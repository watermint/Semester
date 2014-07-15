package etude.pintxos.pocket.domain.infrastructure.auth

import java.net.{URI, URLEncoder}
import java.util.concurrent.{ExecutorService, Executors}

import akka.actor.ActorSystem
import etude.gazpacho.http.{AsyncClient, AsyncClientContext}
import etude.gazpacho.spray.SecureConfiguration
import org.json4s.JsonDSL._
import org.json4s._
import org.json4s.native.JsonMethods._
import spray.http.{HttpCookie, StatusCodes}
import spray.routing.{Route, SimpleRoutingApp}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object AuthService
  extends App
  with SimpleRoutingApp
  with SecureConfiguration {

  val serverInterface = "localhost"
  val serverPort = 7443
  val defaultRedirectUri = s"https://$serverInterface:$serverPort/auth/callback"
  val defaultConsumerKey = "26663-4732e33333464d2f63b63ed3"
  val consumerKey = System.getProperty("etude.bookmark.pocket.consumerKey", defaultConsumerKey)

  val executorsPool: ExecutorService = Executors.newCachedThreadPool()
  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  implicit val system = ActorSystem("Pocket")

  startServer(interface = serverInterface, port = serverPort) {
    routeTop() ~ routeAuthRequest() ~ routeAuthCallback()
  }

  def routeTop(): Route = {
    path("") {
      onComplete(acquireCode()) {
        case Success(code) =>
          setCookie(new HttpCookie(
            name = "code",
            content = code,
            secure = true,
            httpOnly = true,
            maxAge = Some(600)
          )) {
            complete {
              <body>
                <h1>Issue New Token</h1>
                <a href="/auth/request">Connect with Pocket</a>
              </body>
            }
          }
        case Failure(f) =>
          complete(
            StatusCodes.ServiceUnavailable,
            <body>Failed to start authorization process with error:
              {f}
            </body>
          )
      }
    }
  }

  def routeAuthRequest(): Route = {
    path("auth" / "request") {
      get {
        optionalCookie("code") {
          case Some(code) =>
            redirect(redirectUri(code.content).toString, StatusCodes.Found)
          case None =>
            redirect("/", StatusCodes.Found)
        }
      }
    }
  }

  def routeAuthCallback(): Route = {
    path("auth" / "callback") {
      get {
        optionalCookie("code") {
          case Some(code) =>
            onComplete(authorize(code.content)) {
              case Success(session) =>
                AuthSession.storeSession(session)
                complete {
                  <body>Authorization succeed. Session stored on your home directory.</body>
                }
              case Failure(f) =>
                complete {
                  <body>Failed authorization with Pocket.</body>
                }
            }
          case None =>
            redirect("/", StatusCodes.Found)
        }
      }
    }
  }

  def acquireCode(): Future[String] = {
    val client = AsyncClient(AsyncClientContext(executionContext = executors))
    val requestContent = Map(
      "consumer_key" -> consumerKey,
      "redirect_uri" -> defaultRedirectUri
    )

    client.postWithString(
      new URI("https://getpocket.com/v3/oauth/request"),
      compact(render(requestContent)),
      Map(
        "Content-type" -> "application/json; charset=UTF-8",
        "X-Accept" -> "application/json"
      )
    ) map {
      response =>
        val r: Seq[String] = for {
          JObject(result) <- response.contentAsJson.get
          JField("code", JString(code)) <- result
        } yield {
          code
        }
        r.last
    }
  }

  def redirectUri(code: String, redirectUri: URI = new URI(defaultRedirectUri)): URI = {
    new URI(
      "https://getpocket.com/auth/authorize?request_token=" +
        code + "&redirect_uri=" + URLEncoder.encode(redirectUri.toString, "UTF-8")
    )
  }

  def authorize(code: String): Future[AuthSession] = {
    val client = AsyncClient(AsyncClientContext(executionContext = executors))
    val requestContent = Map(
      "consumer_key" -> consumerKey,
      "code" -> code
    )

    client.postWithString(
      new URI("https://getpocket.com/v3/oauth/authorize"),
      compact(render(requestContent)),
      Map(
        "Content-type" -> "application/json; charset=UTF-8",
        "X-Accept" -> "application/json"
      )
    ) map {
      response =>
        val r: Seq[AuthSession] = for {
          JObject(result) <- response.contentAsJson.get
          JField("access_token", JString(accessToken)) <- result
          JField("username", JString(username)) <- result
        } yield {
          AuthSession(
            userName = username,
            accessToken = accessToken,
            consumerKey = consumerKey
          )
        }
        r.last
    }
  }

}
