package etude.pintxos.chatwork.domain.service.v0.parser

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.V0AsyncEntityIO
import etude.pintxos.chatwork.domain.service.v0.model.RoomUpdateInfo
import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room.RoomId
import org.json4s.JsonAST.{JField, JInt, JObject, JString}
import org.json4s._

import scala.concurrent.Future

object GetUpdateParser
  extends V0AsyncEntityIO
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

  def parseRoomUpdateInfo(json: JValue)(implicit context: EntityIOContext[Future]): Seq[RoomUpdateInfo] = {
    implicit val executionContext = getExecutionContext(context)
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
