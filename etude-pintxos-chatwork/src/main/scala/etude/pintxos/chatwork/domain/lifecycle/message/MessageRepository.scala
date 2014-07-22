package etude.pintxos.chatwork.domain.lifecycle.message

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.model.message.{Message, MessageId, Text}
import etude.pintxos.chatwork.domain.model.room.{Room, RoomId}

import scala.language.higherKinds

trait MessageRepository[M[+A]]
  extends MessageReader[M] {

  def say(text: Text)(room: Room)(implicit context: EntityIOContext[M]): M[Option[MessageId]]

  def markAsRead(message: MessageId)(implicit context: EntityIOContext[M]): M[MessageId]

  def messages(roomId: RoomId, from: MessageId, count: Int)(implicit context: EntityIOContext[M]): M[List[Message]]
}
