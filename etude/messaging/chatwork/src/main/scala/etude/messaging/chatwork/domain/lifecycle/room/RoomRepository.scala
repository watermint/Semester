package etude.messaging.chatwork.domain.lifecycle.room

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityIOContext
import etude.messaging.chatwork.domain.model.message.MessageId
import etude.messaging.chatwork.domain.model.room.{RoomId, Room}

trait RoomRepository[M[+A]]
  extends RoomReader[M] {

  def myRoom()(implicit context: EntityIOContext[M]): M[Room]

  def rooms()(implicit context: EntityIOContext[M]): M[List[Room]]

  def latestMessage(roomId: RoomId)(implicit context: EntityIOContext[M]): M[MessageId]
}
