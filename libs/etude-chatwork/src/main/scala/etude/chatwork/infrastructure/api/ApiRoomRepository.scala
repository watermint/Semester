package etude.chatwork.infrastructure.api

import scala.util.Try
import etude.chatwork.domain.room._
import etude.chatwork.domain.message.MessageId
import etude.chatwork.infrastructure.api.v1.V1RoomRepository
import etude.chatwork.infrastructure.api.v0.V0RoomRepository

class ApiRoomRepository(implicit authContext: MixedAuthContext) extends RoomRepository {
  def rooms(): Try[List[Room]] =
    V1RoomRepository().rooms()

  def create(name: String, roles: List[RoomRole], description: String, icon: RoomIcon): Try[RoomId] =
    V1RoomRepository().create(name, roles, description, icon)

  def latestMessage(roomId: RoomId): Try[MessageId] =
    V0RoomRepository().latestMessage(roomId)

  def markAsRead(message: MessageId): Try[MessageId] =
    V0RoomRepository().markAsRead(message)

  def resolve(identifier: RoomId): Try[Room] =
    V1RoomRepository().resolve(identifier)
}
