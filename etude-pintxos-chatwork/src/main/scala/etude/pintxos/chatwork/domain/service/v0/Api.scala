package etude.pintxos.chatwork.domain.service.v0

import java.net.URI

import etude.epice.http._
import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.V0AsyncEntityIO
import etude.pintxos.chatwork.domain.service.v0.auth.Auth
import org.json4s._

import scala.concurrent.Future
import scala.language.higherKinds
import scala.util.{Failure, Success, Try}

object Api
  extends V0AsyncEntityIO {

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

  def login(implicit context: EntityIOContext[Future]): Try[Boolean] = {
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
                      (implicit context: EntityIOContext[Future]): URI = {

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

  private[v0] def apiResponseParser(command: String, response: Response): Try[JValue] = {
    response.contentAsJson map {
      json =>
        val JBool(success) = json \ "status" \ "success"
        val result = json \ "result"

        if (success) {
          result
        } else {
          val JString(message) = json \ "status" \ "message"
          if (message.contains("NO LOGIN")) {
            throw SessionTimeoutException(message)
          } else {
            throw CommandFailureException(command, message)
          }
        }
    }
  }

  def api(command: String,
          params: Map[String, String],
          data: Map[String, String] = Map())
         (implicit context: EntityIOContext[Future]): JValue = {
    if (!hasToken(context)) {
      throw NoSessionAvailableException()
    }

    val gatewayUri = apiUri(command, params)
    val client = getClient(context)
    val response = data.size match {
      case 0 => client.get(gatewayUri).get
      case _ => client.post(gatewayUri, data).get
    }

    apiResponseParser(command, response) match {
      case Failure(e) => throw e
      case Success(result) => result
    }
  }


}
