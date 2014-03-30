package etude.messaging.chatwork.domain.room

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityIOContext
import etude.messaging.chatwork.domain.message.MessageId

trait RoomRepository[M[+A]]
  extends RoomReader[M] {

  def myRoom()(implicit context: EntityIOContext[M]): M[Room]

  def rooms()(implicit context: EntityIOContext[M]): M[List[Room]]

  def latestMessage(roomId: RoomId)(implicit context: EntityIOContext[M]): M[MessageId]
}
