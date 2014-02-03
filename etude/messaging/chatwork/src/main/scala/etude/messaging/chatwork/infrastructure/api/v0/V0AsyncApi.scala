package etude.messaging.chatwork.infrastructure.api.v0

import java.net.URI
import scala.Some
import scala.language.higherKinds
import scala.util.{Success, Failure, Try}
import etude.messaging.chatwork.infrastructure.api.{QoSException, ApiQoS}
import org.json4s._
import etude.foundation.http._
import etude.foundation.http.{Response, Client}
import scala.concurrent.{future, Future}
import etude.foundation.domain.lifecycle.EntityIOContext
import etude.foundation.domain.lifecycle.async.AsyncEntityIO

object V0AsyncApi
  extends V0EntityIO[Future]
  with AsyncEntityIO
  with ApiQoS {

  def isKddiChatwork(implicit context: EntityIOContext[Future]): Boolean = {
    getOrganizationId(context) match {
      case Some(o) => true
      case _ => false
    }
  }

  def baseUri(implicit context: EntityIOContext[Future]): URI = {
    if (isKddiChatwork) {
      new URI("https://kcw.kddi.ne.jp/")
    } else {
      new URI("https://www.chatwork.com/")
    }
  }

  private[v0] def login(implicit context: EntityIOContext[Future]): Try[Boolean] = {
    val client = getClient(context)

    val loginUri = getOrganizationId(context) match {
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
        "email" -> getEmail(context),
        "password" -> getPassword(context)
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
                setAccessToken(Some(token.group(1)), context)
                setMyId(Some(myId.group(1)), context)
                Success(true)
              case _ =>
                Failure(V0LoginFailedException("Invalid email or password"))
            }
        }
    }
  }

  protected def apiUri(command: String,
                       params: Map[String, String])
                      (implicit context: EntityIOContext[Future]): URI = {

    (getMyId(context), getAccessToken(context)) match {
      case (Some(myId), Some(token)) =>
        baseUri.withPath("/gateway.php")
          .withQuery("cmd", command)
          .withQuery(params.toList)
          .withQuery("myid", myId)
          .withQuery("_v", "1.80a")
          .withQuery("_av", "4")
          .withQuery("_t", token)
          .withQuery("ln", "en")
          .withQuery("_", System.currentTimeMillis().toString)
      case _ =>
        throw new IllegalStateException(s"$context has no accessToken")
    }
  }

  private[v0] def apiResponseParser(command: String, response: Response): Try[JValue] = {
    val JBool(success) = response.contentAsJson \ "status" \ "success"
    val result = response.contentAsJson \ "result"

    if (success) {
      Success(result)
    } else {
      val JString(message) = response.contentAsJson \ "status" \ "message"
      if (message.contains("NO LOGIN")) {
        Failure(V0SessionTimeoutException(message))
      } else {
        Failure(V0CommandFailureException(command, message))
      }
    }
  }

  private[v0] def syncApi(command: String,
                        params: Map[String, String],
                        retries: Int = 2)
                       (implicit context: EntityIOContext[Future]): JValue = {
    ApiQoS.throttle.execute {
      if (!hasToken(context)) {
        login
      }
      val gatewayUri = apiUri(command, params)
      val response = getClient(context).get(gatewayUri) match {
        case Failure(e) => throw e
        case Success(r) => r
      }

      apiResponseParser(command, response) match {
        case Failure(e: V0SessionTimeoutException) =>
          clearToken(context)
          if (retries > 0) {
            syncApi(command, params, retries - 1)
          } else {
            throw V0CommandFailureException(command, "Exceeds retry count")
          }
        case Failure(e) => throw e
        case Success(result) => result
      }
    }
  }

  def api(command: String,
          params: Map[String, String],
          retries: Int = 2)
         (implicit context: EntityIOContext[Future]): Future[JValue] = {
    implicit val executor = getExecutionContext(context)

    future {
      syncApi(command, params, retries)
    }
  }
}
