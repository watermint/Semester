package etude.messaging.chatwork.domain.infrastructure.v0.auth

import scala.util.Try
import etude.messaging.chatwork.domain.infrastructure.v0.V0UnknownChatworkProtocolException

class Basic extends Auth {
  def acceptable(context: AuthContext): Boolean = {
    context.redirectedUri.isEmpty
  }

  def acquireToken(context: AuthContext): Try[AuthToken] = {
    submitLogin(context)
  }

  private def submitLogin(context: AuthContext): Try[AuthToken] = {
    context.client.post(
      uri = context.startPageUri,
      formData = List(
        "email" -> context.username,
        "password" -> context.password
      )
    ) flatMap {
      r =>
        getTopPage(context)
    }
  }

  private def getTopPage(context: AuthContext): Try[AuthToken] = {
    context.client.get(context.startPageUri) map {
      responseOfTop =>
        parsePage(responseOfTop.contentAsString) match {
          case Some(t) => t
          case _ => throw new V0UnknownChatworkProtocolException("No Access Token found")
        }
    }
  }
}
