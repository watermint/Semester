package etude.messaging.chatwork.domain.infrastructure.api.v0.auth

import scala.util.Try
import scala.concurrent.Future
import java.net.URI
import etude.messaging.chatwork.domain.infrastructure.api.v0.{V0UnknownChatworkProtocolException, V0AsyncApi, V0EntityIO}
import etude.foundation.http._
import etude.foundation.domain.lifecycle.EntityIOContext

trait Auth {
  def acceptable(context: AuthContext): Boolean

  def acquireToken(context: AuthContext): Try[AuthToken]

  def parsePage(content: String): Option[AuthToken] = {
    (
      """var ACCESS_TOKEN = '(\w+)';""".r.findFirstMatchIn(content),
      """var myid = '(\d+)';""".r.findFirstMatchIn(content)
      ) match {
      case (Some(token), Some(myId)) =>
        Some(
          AuthToken(
            myId = myId.group(1),
            accessToken = token.group(1)
          )
        )
      case _ =>
        None
    }
  }
}

object Auth extends V0EntityIO[Future] {
  val providers = Seq(
    new Basic,
    new Exaggerated
  )

  def loginUri(implicit context: EntityIOContext[Future]): URI = {
    val baseUri: URIContainer = V0AsyncApi.baseUri
    getOrganizationId(context) match {
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
  }

  def login(implicit context: EntityIOContext[Future]): Try[AuthToken] = {
    val client = getClient(context)

    client.get(loginUri) flatMap {
      r =>
        val ac = AuthContext(
          client,
          getUsername(context),
          getPassword(context),
          r.redirectLocation(),
          loginUri,
          r.contentAsString
        )

        providers.find(_.acceptable(ac)) match {
          case Some(provider) =>
            provider.acquireToken(ac)
          case _ =>
            throw new V0UnknownChatworkProtocolException("Unsupported authentication method")
        }
    }
  }
}