package etude.pintxos.chatwork.domain.infrastructure.api.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.V0AsyncUpdate._
import etude.pintxos.chatwork.domain.infrastructure.api.v0.model.UpdateInfo
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncApi, V0AsyncEntityIO}
import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room.RoomId
import org.json4s.JsonAST.{JField, JObject}
import org.json4s._

import scala.concurrent.Future

object GetUpdate extends V0AsyncEntityIO {

  def update(updateLastId: Boolean = true)(implicit context: EntityIOContext[Future]): Future[List[UpdateInfo]] = {
    implicit val executor = getExecutionContext(context)

    getLastId(context) match {
      case Some(lastId) =>
        V0AsyncApi.api("get_update", Map("last_id" -> lastId)) map {
          json =>
            if (updateLastId) {
              parseLastId(json) match {
                case Some(newLastId) => setLastId(newLastId, context)
                case _ =>
              }
            }
            parseUpdateInfo(json)
        }
      case _ =>
        throw new IllegalStateException("No last_id found in the context")
    }
  }

  def parseUpdateInfo(json: JValue)(implicit context: EntityIOContext[Future]): List[UpdateInfo] = {
    implicit val executionContext = getExecutionContext(context)
    for {
      JObject(update) <- json
      JField("update_info", JObject(updateInfo)) <- update
      JField("room", JObject(roomData)) <- updateInfo
      JField(roomIdValue, JObject(roomUpdate)) <- roomData
    } yield {
      val roomDataMap = roomData.toMap
      val roomId = new RoomId(roomIdValue.toLong)

      UpdateInfo(
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
