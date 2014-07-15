package etude.pintxos.chatwork.domain.lifecycle.room

import etude.domain.core.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room.{Room, RoomId}

import scala.language.higherKinds

trait RoomRepository[M[+A]]
  extends RoomReader[M] {

  def myRoom()(implicit context: EntityIOContext[M]): M[Room]

  def rooms()(implicit context: EntityIOContext[M]): M[List[Room]]

  def latestMessage(roomId: RoomId)(implicit context: EntityIOContext[M]): M[MessageId]
}
