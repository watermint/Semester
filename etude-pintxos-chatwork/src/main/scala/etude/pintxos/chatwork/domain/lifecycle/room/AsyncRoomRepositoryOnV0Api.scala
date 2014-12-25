package etude.pintxos.chatwork.domain.lifecycle.room

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.command.{InitLoad, GetRoomInfo, LoadChat}
import etude.pintxos.chatwork.domain.infrastructure.api.v0.V0AsyncApi
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.{LoadChatRequest, InitLoadRequest, GetRoomInfoRequest}
import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room._
import org.json4s._

import scala.concurrent._

private[room]
class AsyncRoomRepositoryOnV0Api
  extends AsyncRoomRepository {

  type This <: AsyncRoomRepositoryOnV0Api

  def create(name: String, description: String, icon: RoomIcon)(implicit context: EntityIOContext[Future]): Future[RoomId] = ???

  def resolve(identity: RoomId)(implicit context: EntityIOContext[Future]): Future[Room] = {
    implicit val executor = getExecutionContext(context)
    InitLoad.execute(InitLoadRequest()) flatMap {
      p =>
        GetRoomInfo.execute(GetRoomInfoRequest(identity)) map {
          r =>
            r.room
        }
    }
  }

  def containsByIdentity(identity: RoomId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executor = getExecutionContext(context)
    InitLoad.execute(InitLoadRequest()) map {
      p =>
        p.rooms.exists(_.roomId.equals(identity))
    }
  }

  def myRoom()(implicit context: EntityIOContext[Future]): Future[Room] = {
    implicit val executor = getExecutionContext(context)
    InitLoad.execute(InitLoadRequest()) map {
      p =>
        p.rooms.map { r => r -> r.roomType }.collect {
          case (r, t: RoomTypeMy) => r
        }.last
    }
  }

  def rooms()(implicit context: EntityIOContext[Future]): Future[List[Room]] = {
    implicit val executor = getExecutionContext(context)
    InitLoad.execute(InitLoadRequest()) map {
      p =>
        p.rooms
    }
  }

  def latestMessage(roomId: RoomId)(implicit context: EntityIOContext[Future]): Future[MessageId] = {
    implicit val executor = getExecutionContext(context)
    LoadChat.execute(LoadChatRequest(roomId)) map {
      r =>
        r.chatList.map(_.messageId).maxBy(_.messageId)
    }
  }
}
