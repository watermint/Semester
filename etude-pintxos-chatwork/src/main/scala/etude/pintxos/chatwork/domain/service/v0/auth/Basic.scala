package etude.pintxos.chatwork.domain.service.v0.auth

import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.service.v0.V0UnknownChatworkProtocolException

import scala.util.Try

class Basic extends Auth {
  val logger = LoggerFactory.getLogger(getClass)

  def acceptable(context: AuthContext): Boolean = {
    true
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
