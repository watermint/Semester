package etude.messaging.chatwork.domain.infrastructure.api.v0.auth

import etude.messaging.chatwork.domain.infrastructure.api.v0.V0UnknownChatworkProtocolException

import scala.util.Try

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
      formData = Map(
        "email" -> context.username,
        "password" -> context.password
      )
    ) flatMap {
      r =>
        getTopPage(context)
    }
  }

  private def getTopPage(context: AuthContext): Try[AuthToken] = {
    context.client.get(context.startPageUri) flatMap {
      responseOfTop =>
        responseOfTop.contentAsString map {
          page =>
            parsePage(page) match {
              case Some(t) => t
              case _ => throw new V0UnknownChatworkProtocolException("No Access Token found")
            }
        }
    }
  }
}
