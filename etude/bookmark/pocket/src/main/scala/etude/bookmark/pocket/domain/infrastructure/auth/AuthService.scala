package etude.bookmark.pocket.domain.infrastructure.auth

import com.twitter.finatra.FinatraServer
import etude.foundation.http.SyncClient
import java.net.{URLEncoder, URI}
import scala.util.Try
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

object AuthService extends FinatraServer {

  System.setProperty("com.twitter.finatra.config.certificatePath", "etude/bookmark/pocket/src/cert/server.crt")
  System.setProperty("com.twitter.finatra.config.keyPath", "etude/bookmark/pocket/src/cert/server.key")

  val defaultRedirectUri = "https://localhost:7443/auth/callback"
  val defaultConsumerKey = "26663-4732e33333464d2f63b63ed3"
  val consumerKey = System.getProperty("etude.bookmark.pocket.consumerKey", defaultConsumerKey)

  register(new AuthController())

  def acquireCode(): Try[String] = {
    val client = SyncClient()
    val requestContent = Map(
      "consumer_key" -> consumerKey,
      "redirect_uri" -> defaultRedirectUri
    )

    client.postWithString(
      new URI("https://getpocket.com/v3/oauth/request"),
      compact(render(requestContent)),
      List(
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

  def authorize(code: String): Try[AuthSession] = {
    val client = SyncClient()
    val requestContent = Map(
      "consumer_key" -> consumerKey,
      "code" -> code
    )

    client.postWithString(
      new URI("https://getpocket.com/v3/oauth/authorize"),
      compact(render(requestContent)),
      List(
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
