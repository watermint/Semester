package etude.messaging.chatwork.domain.infrastructure.v0

case class V0UnknownChatworkProtocolException(message: String, payload: Option[String] = None) extends RuntimeException(message)
