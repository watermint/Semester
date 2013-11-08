package etude.chatwork

import java.time.Instant

case class Message(aid: BigInt,
                   roomId: BigInt,
                   messageId: BigInt,
                   message: String,
                   timestamp: Instant)

object Message {
  def fromChatList(roomId: BigInt, chat: Map[String, Any]): Message = {
    Message(
      aid = chat.get("aid").get.asInstanceOf[BigInt],
      roomId = roomId,
      messageId = chat.get("id").get.asInstanceOf[BigInt],
      message = chat.get("msg").get.asInstanceOf[String],
      timestamp = Instant.ofEpochSecond(chat.get("tm").get.asInstanceOf[BigInt].longValue())
    )
  }
}