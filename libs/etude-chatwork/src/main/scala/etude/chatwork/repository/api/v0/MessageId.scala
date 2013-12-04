package etude.chatwork.repository.api.v0

case class MessageId(messageId: String) {
  lazy val id: BigInt = BigInt(messageId)
}

object MessageId {
  lazy val EPOCH = MessageId(0)

  def apply(messageId: BigInt): MessageId = MessageId(messageId.toString())
}