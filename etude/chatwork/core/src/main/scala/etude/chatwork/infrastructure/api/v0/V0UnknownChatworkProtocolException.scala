package etude.chatwork.infrastructure.api.v0

case class V0UnknownChatworkProtocolException(message: String, payload: Option[String] = None) extends RuntimeException(message)
