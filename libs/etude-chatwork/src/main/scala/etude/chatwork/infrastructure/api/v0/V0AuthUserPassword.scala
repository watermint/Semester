package etude.chatwork.infrastructure.api.v0

import etude.http._
import java.net.URI
import java.time.Instant
import scala.Some
import etude.http.Client
import scala.util.{Success, Failure, Try}
import etude.chatwork.infrastructure.api.{QoSException, PasswordAuthentication, ApiQoS}
import org.json4s._

case class V0AuthUserPassword(user: String,
                            password: String,
                            organizationId: Option[String])
  extends PasswordAuthentication
  with ApiQoS {

  lazy val isKddiChatwork: Boolean = {
    organizationId match {
      case Some(o) => true
      case _ => false
    }
  }

  lazy val baseUri: URI = {
    if (isKddiChatwork) {
      new URI("https://kcw.kddi.ne.jp/")
    } else {
      new URI("https://www.chatwork.com/")
    }
  }

  lazy val imageBaseUri: URI = new URI("https://tky-chat-work-appdata.s3.amazonaws.com/avatar/")

  protected var currentContext: Option[V0SessionContext] = None

  protected def login: Try[V0SessionContext] = {
    if (shouldFail("login")) {
      return Failure(QoSException("login"))
    }

    val client = Client()

    val loginUri = organizationId match {
      case Some(s) =>
        baseUri
          .withPath("/login.php")
          .withQuery("s" -> s)
          .withQuery("lang" -> "en")
          .withQuery("package" -> "chatwork")
      case _ =>
        baseUri
          .withPath("/login.php")
          .withQuery("lang" -> "en")
    }

    client.post(
      uri = loginUri,
      formData = List(
        "email" -> user,
        "password" -> password
      )
    ) match {
      case Failure(e) => Failure(e)
      case _ =>
        client.get(baseUri) match {
          case Failure(e) => Failure(e)
          case Success(r) =>
            val accessTokenRegex = """var ACCESS_TOKEN = '(\w+)';""".r
            val myIdRegex = """var myid = '(\d+)';""".r

            (accessTokenRegex.findFirstMatchIn(r.contentAsString), myIdRegex.findFirstMatchIn(r.contentAsString)) match {
              case (Some(token), Some(myId)) =>
                val sc = V0SessionContext(client, token.group(1), myId.group(1), Instant.now())
                currentContext = Some(sc)
                Success(sc)
              case _ =>
                Failure(V0LoginFailedException("Invalid email or password"))
            }
        }
    }
  }

  private def apiUri(command: String, params: Map[String, String], context: V0SessionContext): URI = {
    baseUri.withPath("/gateway.php")
      .withQuery("cmd", command)
      .withQuery(params.toList)
      .withQuery("myid", context.myId)
      .withQuery("_v", "1.80a")
      .withQuery("_av", "4")
      .withQuery("_t", context.accessToken)
      .withQuery("ln", "en")
      .withQuery("_", System.currentTimeMillis().toString)
  }

  private def apiResponseParser(command: String, response: Response): Try[JValue] = {
    val JBool(success) = response.contentAsJson \ "status" \ "success"
    val JString(message) = response.contentAsJson \ "status" \ "message"
    val result = response.contentAsJson \ "result"

    if (success) {
      Success(result)
    } else {
      if (message.contains("NO LOGIN")) {
        Failure(V0SessionTimeoutException(message))
      } else {
        Failure(V0CommandFailureException(command, message))
      }
    }
  }

  def api(command: String,
          params: Map[String, String],
          retries: Int = 2): Try[JValue] = {

    ApiQoS.throttle.execute {
      val context: V0SessionContext = currentContext match {
        case Some(c) => c
        case None => login match {
          case Failure(e) => return Failure(e)
          case Success(c) => c
        }
      }
      val gatewayUri = apiUri(command, params, context)
      context.client.get(gatewayUri) match {
        case Failure(e: V0SessionTimeoutException) =>
          currentContext = None
          if (retries > 0) {
            api(command, params, retries - 1)
          } else {
            Failure(V0CommandFailureException(command, "Exceeds retry count"))
          }
        case Failure(e) => Failure(e)
        case Success(r) => apiResponseParser(command, r)
      }
    }
  }
}
