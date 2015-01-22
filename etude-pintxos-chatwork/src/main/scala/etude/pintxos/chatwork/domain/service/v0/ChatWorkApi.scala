package etude.pintxos.chatwork.domain.service.v0

import java.net.URI

import etude.epice.http._
import etude.pintxos.chatwork.domain.service.v0.auth.Auth
import org.json4s._

import scala.util.{Failure, Success, Try}

object ChatWorkApi
  extends ChatWorkEntityIO {

  def isKddiChatwork(implicit context: ChatWorkIOContext): Boolean = {
    getOrganizationId(context) match {
      case Some(o) => true
      case _ => false
    }
  }

  def baseUri(implicit context: ChatWorkIOContext): URI = {
    if (isKddiChatwork) {
      new URI("https://kcw.kddi.ne.jp/")
    } else {
      new URI("https://www.chatwork.com/")
    }
  }

  def login(implicit context: ChatWorkIOContext): Try[Boolean] = {
    Auth.login map {
      token =>
        setAccessToken(token.accessToken, context)
        setMyId(token.myId, context)
        true
    } recover {
      case e: Exception =>
        throw new IllegalStateException("Login failed", e)
    }
  }

  protected def apiUri(command: String,
                       params: Map[String, String])
                      (implicit context: ChatWorkIOContext): URI = {

    (getMyId(context), getAccessToken(context)) match {
      case (Some(myId), Some(token)) =>
        baseUri.withPath("/gateway.php")
          .withQuery("cmd", command)
          .withQuery(params)
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

  private[v0] def apiResponseParser(command: String,
                                    response: Response,
                                    params: Map[String, String]): Try[JValue] = {
    response.contentAsJson map {
      json =>
        val JBool(success) = json \ "status" \ "success"
        val result = json \ "result"

        if (success) {
          result
        } else {
          json match {
            case JObject(j) =>
              val JString(message) = json \ "status" \ "message"
              if (message.contains("NO LOGIN")) {
                throw SessionTimeoutException(message)
              } else {
                throw CommandFailureException(command, message)
              }
            case JArray(a) =>
              val JString(message) = a.head
              if (message.contains("You don't have permission")) {
                throw CommandPermissionException(command, params)
              } else {
                throw CommandFailureException(command, message)
              }
            case j =>
              throw CommandFailureException(command, s"Unexpected JSON format: $j")
          }
        }
    }
  }

  def api(command: String,
          params: Map[String, String],
          data: Map[String, String] = Map())
         (implicit context: ChatWorkIOContext): JValue = {
    if (!hasToken(context)) {
      throw NoSessionAvailableException()
    }

    val gatewayUri = apiUri(command, params)
    val client = getClient(context)
    val response = data.size match {
      case 0 => client.get(gatewayUri).get
      case _ => client.post(gatewayUri, data).get
    }

    apiResponseParser(command, response, params) match {
      case Failure(e) => throw e
      case Success(result) => result
    }
  }


}
