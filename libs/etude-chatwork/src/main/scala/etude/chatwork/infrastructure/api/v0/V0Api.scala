package etude.chatwork.infrastructure.api.v0

import etude.http._
import java.net.URI
import scala.Some
import etude.http.Client
import scala.util.{Success, Failure, Try}
import etude.chatwork.infrastructure.api.{QoSException, ApiQoS}
import org.json4s._

object V0Api extends ApiQoS {

  def isKddiChatwork(implicit sessionContext: V0SessionContext): Boolean = {
    sessionContext.organizationId match {
      case Some(o) => true
      case _ => false
    }
  }

  def baseUri(implicit sessionContext: V0SessionContext): URI = {
    if (isKddiChatwork) {
      new URI("https://kcw.kddi.ne.jp/")
    } else {
      new URI("https://www.chatwork.com/")
    }
  }

  protected def login(implicit sessionContext: V0SessionContext): Try[V0SessionTokens] = {
    if (shouldFail("login")) {
      return Failure(QoSException("login"))
    }

    val client = Client()

    val loginUri = sessionContext.organizationId match {
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
        "email" -> sessionContext.email,
        "password" -> sessionContext.password
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
                val tokens = V0SessionTokens(client, token.group(1), myId.group(1))
                sessionContext.tokens = Some(tokens)
                Success(tokens)
              case _ =>
                Failure(V0LoginFailedException("Invalid email or password"))
            }
        }
    }
  }

  private def apiUri(command: String,
                     params: Map[String, String],
                     tokens: V0SessionTokens)
                    (implicit sessionContext: V0SessionContext): URI = {

    baseUri.withPath("/gateway.php")
      .withQuery("cmd", command)
      .withQuery(params.toList)
      .withQuery("myid", tokens.myId)
      .withQuery("_v", "1.80a")
      .withQuery("_av", "4")
      .withQuery("_t", tokens.accessToken)
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
          retries: Int = 2)
         (implicit sessionContext: V0SessionContext): Try[JValue] = {

    ApiQoS.throttle.execute {
      val tokens: V0SessionTokens = sessionContext.tokens match {
        case Some(t) => t
        case None => login match {
          case Failure(e) => return Failure(e)
          case Success(t) => t
        }
      }
      val gatewayUri = apiUri(command, params, tokens)
      tokens.client.get(gatewayUri) match {
        case Failure(e: V0SessionTimeoutException) =>
          sessionContext.tokens = None
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
