package etude.pintxos.chatwork.domain.lifecycle.room

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.command.{GetRoomInfo, LoadChat}
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncApi, V0AsyncInitLoad}
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
    V0AsyncInitLoad.initLoad() flatMap {
      p =>
        GetRoomInfo.room(identity) map {
          r =>
            r._1
        }
    }
  }

  def containsByIdentity(identity: RoomId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executor = getExecutionContext(context)
    V0AsyncInitLoad.initLoad() map {
      p =>
        p.rooms.exists(_.roomId.equals(identity))
    }
  }

  def myRoom()(implicit context: EntityIOContext[Future]): Future[Room] = {
    implicit val executor = getExecutionContext(context)
    V0AsyncInitLoad.initLoad() map {
      p =>
        p.rooms.map { r => r -> r.roomType }.collect {
          case (r, t: RoomTypeMy) => r
        }.last
    }
  }

  def rooms()(implicit context: EntityIOContext[Future]): Future[List[Room]] = {
    implicit val executor = getExecutionContext(context)
    V0AsyncInitLoad.initLoad() map {
      p =>
        p.rooms
    }
  }

  def latestMessage(roomId: RoomId)(implicit context: EntityIOContext[Future]): Future[MessageId] = {
    implicit val executor = getExecutionContext(context)
    LoadChat.loadChat(roomId) map {
      r =>
    }
    V0AsyncApi.api(
      "load_chat",
      Map(
        "room_id" -> roomId.value.toString(),
        "first_chat_id" -> "0",
        "last_chat_id" -> "0",
        "jump_to_chat_id" -> "0",
        "unread_num" -> "0",
        "desc" -> "0",
        "task" -> "0"
      )
    ) map {
      json =>
        val messageIds: List[BigInt] = for {
          JObject(data) <- json
          JField("chat_list", JArray(messages)) <- data
          JObject(message) <- messages
          JField("id", JInt(messageId)) <- message
        } yield {
          messageId
        }
        new MessageId(roomId, messageIds.maxBy(identity))
    }
  }
}
