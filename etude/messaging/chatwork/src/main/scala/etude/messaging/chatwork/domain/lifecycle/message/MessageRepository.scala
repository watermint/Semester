package etude.messaging.chatwork.domain.lifecycle.message

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityIOContext
import etude.messaging.chatwork.domain.model.room.{Room, RoomId}
import etude.messaging.chatwork.domain.model.message.{Text, Message, MessageId}

trait MessageRepository[M[+A]]
  extends MessageReader[M] {

  def say(text: Text)(room: Room)(implicit context: EntityIOContext[M]): M[Option[MessageId]]

  def markAsRead(message: MessageId)(implicit context: EntityIOContext[M]): M[MessageId]

  def messages(roomId: RoomId, from: MessageId, count: Int)(implicit context: EntityIOContext[M]): M[List[Message]]
}
