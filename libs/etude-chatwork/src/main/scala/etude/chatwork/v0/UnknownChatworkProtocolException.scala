package etude.chatwork.v0

case class UnknownChatworkProtocolException(message: String, payload: Option[String] = None) extends RuntimeException(message)

object UnknownChatworkProtocolException {
  def apply(message: String, payload: String): UnknownChatworkProtocolException =
    UnknownChatworkProtocolException(message, Some(payload))

  def apply(message: String): UnknownChatworkProtocolException =
    UnknownChatworkProtocolException(message)
}