package etude.messaging.chatwork.domain.infrastructure.api.v0

import java.net.URI
import scala.Some
import scala.language.higherKinds
import scala.util.{Success, Failure, Try}
import org.json4s._
import etude.foundation.http._
import etude.foundation.http.Response
import scala.concurrent.{future, Future}
import etude.foundation.domain.lifecycle.EntityIOContext
import etude.foundation.domain.lifecycle.async.AsyncEntityIO
import etude.messaging.chatwork.domain.infrastructure.api.v0.auth.Auth
import java.time.Instant
import etude.messaging.chatwork.domain.infrastructure.api.AsyncEntityIOOnV0Api

object V0AsyncApi
  extends V0AsyncEntityIO {

  val loginFailureThreshold = 3
  val loginDuration = 3

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

  private[api] def login(implicit context: EntityIOContext[Future]): Try[Boolean] = {
    beginLogin(context)
    try {
      // assert login failure threshold
      if (getLoginFailure(context).get() >= loginFailureThreshold) {
        return Failure(new IllegalStateException(s"Abort Login: due to failed more than $loginFailureThreshold times"))
      }

      // assert login action time span
      getLoginTime(context) match {
        case Some(t) if t.isBefore(Instant.now.minusSeconds(loginDuration)) =>
          return getAccessToken(context) match {
            case Some(_) => Success(true)
            case _ => Failure(new IllegalStateException("Login QoS exception"))
          }
        case _ =>
      }

      Auth.login map {
        token =>
          setAccessToken(token.accessToken, context)
          setMyId(token.myId, context)
          true
      } recover {
        case _ =>
          getLoginFailure(context).incrementAndGet()
          throw new IllegalStateException("Login failed")
      }
    } finally {
      setLoginTime(Instant.now, context)
      endLogin(context)
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
    response.contentAsJson map {
      json =>
        val JBool(success) = json \ "status" \ "success"
        val result = json \ "result"

        if (success) {
          result
        } else {
          val JString(message) = json \ "status" \ "message"
          if (message.contains("NO LOGIN")) {
            throw V0SessionTimeoutException(message)
          } else {
            throw V0CommandFailureException(command, message)
          }
        }
    }
  }

  private[v0] def syncApi(command: String,
                          params: Map[String, String],
                          data: Map[String, String] = Map(),
                          retries: Int = 1)
                         (implicit context: EntityIOContext[Future]): JValue = {
    V0ApiQoS.throttle.execute {
      if (!hasToken(context)) {
        if (login.isFailure) {
          throw V0CommandFailureException(command, "Login failed")
        }
      }
      val gatewayUri = apiUri(command, params)
      val client = getClient(context)
      val response = data.size match {
        case 0 => client.get(gatewayUri).get
        case _ => client.post(gatewayUri, data.toList).get
      }

      apiResponseParser(command, response) match {
        case Failure(e: V0SessionTimeoutException) =>
          clearToken(context)
          if (retries > 0) {
            syncApi(command, params, data, retries - 1)
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
          data: Map[String, String] = Map(),
          retries: Int = 2)
         (implicit context: EntityIOContext[Future]): Future[JValue] = {
    implicit val executor = getExecutionContext(context)

    future {
      syncApi(command, params, data, retries)
    }
  }
}
