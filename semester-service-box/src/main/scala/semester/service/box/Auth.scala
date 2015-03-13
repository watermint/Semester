package semester.service.box

import java.net.URLEncoder
import java.util.UUID

import akka.actor.ActorSystem
import com.box.sdk.BoxAPIConnection
import org.json4s.DefaultFormats
import org.json4s.native.Serialization
import semester.readymade.spray.SecureConfiguration
import spray.http._
import spray.routing.{Route, SimpleRoutingApp}

import scala.collection.mutable

object Auth
  extends App
  with AppConfig
  with SimpleRoutingApp
  with SecureConfiguration {

  val serverInterface = "localhost"
  val serverPort = 7443
  val defaultRedirectUri = s"https://$serverInterface:$serverPort/auth/callback"
  val csrfTokens = new mutable.HashMap[String, String]()
  val csrfTokenCookieName = s"token${System.currentTimeMillis()}"

  implicit val system = ActorSystem("box")

  startServer(interface = serverInterface, port = serverPort) {
    routeTop() ~ routeAuthCallback()
  }

  def routeTop(): Route = {
    path("") {
      optionalCookie(csrfTokenCookieName) {
        case Some(cookie) =>
          redirect(boxAuthorizeUrl(cookie.content), StatusCodes.Found)
        case None =>
          setCookie(
            new HttpCookie(
              name = csrfTokenCookieName,
              content = issueCsrfToken(),
              secure = true,
              httpOnly = true,
              maxAge = Some(600)
            )
          ) {
            redirect("/", StatusCodes.Found)
          }
      }
    }
  }

  def routeAuthCallback(): Route = {
    path("auth" / "callback") {
      get {
        optionalCookie(csrfTokenCookieName) {
          case None =>
            redirect("/", StatusCodes.Found)
          case Some(cookie) =>
            parameters('code, 'state) {
              (code, state) =>
                routeVerifyCode(code, cookie.content, state)
            }
        }
      }
    }
  }

  def routeVerifyCode(code: String, tokenKey: String, tokenSecret: String): Route = {
    if (tokenSecretForKey(tokenKey).forall(_.equals(tokenSecret))) {
      implicit val formats = DefaultFormats
      val connection = new BoxAPIConnection(clientId, clientSecret, code)
      val token = AuthToken(connection.getAccessToken, connection.getRefreshToken)
      val json = Serialization.write(token)
      complete {
        HttpEntity(
          contentType = ContentTypes.`application/json`,
          string = json
        )
      }
    } else {
      complete {
        <body>Invalid CSRF token found.</body>
      }
    }
  }

  def boxAuthorizeUrl(csrfKey: String): String = {
    s"https://app.box.com/api/oauth2/authorize?response_type=code&client_id=$clientId&state=${tokenSecretForKey(csrfKey).getOrElse("")}&redirect_uri=${URLEncoder.encode(defaultRedirectUri, "UTF-8")}"
  }

  def issueCsrfToken(): String = {
    val tokenKey = UUID.randomUUID().toString
    val tokenSecret = UUID.randomUUID().toString

    csrfTokens.put(tokenKey, tokenSecret)

    tokenKey
  }

  def tokenSecretForKey(key: String): Option[String] = {
    csrfTokens.get(key)
  }
}
