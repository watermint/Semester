package etude.pintxos.chatwork.domain.infrastructure.api.v0

case class V0UnknownChatworkProtocolException(message: String, payload: Option[String] = None) extends RuntimeException(message)
