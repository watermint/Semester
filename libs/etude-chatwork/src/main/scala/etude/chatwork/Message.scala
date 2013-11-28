package etude.chatwork

import java.time.Instant

case class Message(aid: AccountId,
                   roomId: RoomId,
                   messageId: MessageId,
                   message: String,
                   timestamp: Instant)

object Message {
  def fromChatList(roomId: RoomId, chat: Map[String, Any]): Message = {
    Message(
      aid = AccountId(chat.get("aid").get.asInstanceOf[BigInt]),
      roomId = roomId,
      messageId = MessageId(chat.get("id").get.asInstanceOf[BigInt]),
      message = chat.get("msg").get.asInstanceOf[String],
      timestamp = Instant.ofEpochSecond(chat.get("tm").get.asInstanceOf[BigInt].longValue())
    )
  }
}