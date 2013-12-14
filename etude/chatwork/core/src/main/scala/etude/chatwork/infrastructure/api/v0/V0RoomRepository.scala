package etude.chatwork.infrastructure.api.v0

import etude.chatwork.domain.room._
import scala.util.{Success, Failure, Try}
import etude.chatwork.domain.message.MessageId
import etude.chatwork.infrastructure.api.NotImplementedException
import org.json4s._

case class V0RoomRepository(implicit session: V0SessionContext) extends RoomRepository {
  def rooms(): Try[List[Room]] = Failure(NotImplementedException("Migrated to v1 API"))

  def create(name: String, roles: List[RoomRole], description: String, icon: RoomIcon): Try[RoomId] = Failure(NotImplementedException("Migrated to v1 API"))

  def resolve(identifier: RoomId): Try[Room] = Failure(NotImplementedException("Migrated to v1 API"))

  //  def fromLoadChat(roomId: LegacyRoomId, result: Map[String, Any]): LegacyRoom = {
  //    LegacyRoom(
  //      roomId = roomId,
  //      description = result.get("description").get.asInstanceOf[String],
  //      publicDescription = result.get("public_description").get.asInstanceOf[String],
  //      tasks = result.get("task_dat").get match {
  //        case Some(r) => {
  //          r.asInstanceOf[Map[String, Map[String, Any]]].map {
  //            t =>
  //              LegacyTask.fromTaskDat(t._2)
  //          }.toList
  //        }
  //        case _ => Seq()
  //      },
  //      messages = result.get("chat_list").get.asInstanceOf[List[Map[String, Any]]].map {
  //        c =>
  //          LegacyMessage.fromChatList(roomId, c)
  //      }
  //    )
  //  }

  //  def room(roomId: RoomId): Option[Room] = {
  //    api(
  //      "load_chat",
  //      Map(
  //        "room_id" -> roomId.roomId,
  //        "last_chat_id" -> "0",
  //        "first_chat_id" -> "0",
  //        "jump_to_chat_id" -> "0",
  //        "unread_num" -> "0",
  //        "desc" -> "1",
  //        "task" -> "1"
  //      )
  //    ) match {
  //      case Left(e) => throw e
  //      case Right(r) => Some(Room.fromLoadChat(roomId, r.asInstanceOf[Map[String, Any]]))
  //    }
  //  }


  //  def fromChatList(roomId: LegacyRoomId, chat: Map[String, Any]): LegacyMessage = {
  //    LegacyMessage(
  //      aid = LegacyAccountId(chat.get("aid").get.asInstanceOf[BigInt]),
  //      roomId = roomId,
  //      messageId = LegacyMessageId(chat.get("id").get.asInstanceOf[BigInt]),
  //      message = chat.get("msg").get.asInstanceOf[String],
  //      timestamp = Instant.ofEpochSecond(chat.get("tm").get.asInstanceOf[BigInt].longValue())
  //    )
  //  }

  def latestMessage(roomId: RoomId): Try[MessageId] = {
    V0Api.api(
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
    ) match {
      case Failure(f) => Failure(f)
      case Success(json) =>
        val messageIds: List[BigInt] = for {
          JObject(data) <- json
          JField("chat_list", JArray(messages)) <- data
          JObject(message) <- messages
          JField("id", JInt(messageId)) <- message
        } yield {
          messageId
        }
        Success(new MessageId(roomId, messageIds.maxBy(identity)))
    }
  }

  //  def markAsRead(roomId: RoomId, message: Message): Unit = {
  //    api(
  //      "read",
  //      Map(
  //        "room_id" -> roomId.roomId,
  //        "last_chat_id" -> message.messageId.messageId
  //      )
  //    ) match {
  //      case Left(e) => throw e
  //      case Right(r) =>
  //      // response is like :  {"status":{"success":true},"result":{"read_num":372,"mention_num":0}}
  //    }
  //  }

  def markAsRead(message: MessageId): Try[MessageId] = {
    V0Api.api(
      "read",
      Map(
        "room_id" -> message.roomId.value.toString(),
        "last_chat_id" -> message.messageId.toString()
      )
    ) match {
      case Failure(f) => Failure(f)
      case Success(s) => Success(message)
    }
  }

}
