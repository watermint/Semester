package etude.messaging.chatwork.domain.message

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityIOContext
import etude.messaging.chatwork.domain.room.RoomId

trait MessageRepository[M[+A]]
  extends MessageReader[M] {

  def markAsRead(message: MessageId)(implicit context: EntityIOContext[M]): M[MessageId]

  def messages(roomId: RoomId, from: MessageId, count: Int)(implicit context: EntityIOContext[M]): M[List[Message]]
}
