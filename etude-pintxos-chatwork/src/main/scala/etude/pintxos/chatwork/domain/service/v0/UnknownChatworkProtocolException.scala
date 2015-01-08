package etude.pintxos.chatwork.domain.service.v0

case class UnknownChatworkProtocolException(message: String, payload: Option[String] = None) extends Exception(message)
