package semester.service.chatwork.domain.service.v0.parser

import semester.service.chatwork.domain.model.message.MessageId
import semester.service.chatwork.domain.model.room.RoomId
import semester.service.chatwork.domain.service.v0.ChatWorkEntityIO
import semester.service.chatwork.domain.service.v0.model.RoomUpdateInfo
import org.json4s.JsonAST.{JField, JInt, JObject, JString}
import org.json4s._

object GetUpdateParser
  extends ChatWorkEntityIO
  with ParserBase {

  def parseLastId(json: JValue): Option[String] = {
    val lastId: List[String] = for {
      JObject(doc) <- json
      JField("last_id", JString(id)) <- doc
    } yield {
      id
    }
    lastId.lastOption
  }

  def parseRoomUpdateInfo(json: JValue): Seq[RoomUpdateInfo] = {
    for {
      JObject(update) <- json
      JField("update_info", JObject(updateInfo)) <- update
      JField("room", JObject(roomData)) <- updateInfo
      JField(roomIdValue, JObject(roomUpdate)) <- roomData
    } yield {
      val roomDataMap = roomData.toMap
      val roomId = new RoomId(roomIdValue.toLong)

      RoomUpdateInfo(
        roomId = roomId,
        editedMessages = parseMessageIds(roomId, roomDataMap.get("ce")),
        deletedMessages = parseMessageIds(roomId, roomDataMap.get("cd"))
      )
    }
  }

  def parseMessageIds(roomId: RoomId, json: Option[JValue]): Seq[MessageId] = {
    json match {
      case Some(c) =>
        for {
          JObject(ce) <- c
          JField(messageId, JInt(flag)) <- ce
        } yield {
          MessageId(roomId, messageId.toLong)
        }
      case _ => Seq()
    }
  }

}
