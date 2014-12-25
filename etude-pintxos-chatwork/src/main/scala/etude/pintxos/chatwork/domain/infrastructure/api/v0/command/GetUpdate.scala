package etude.pintxos.chatwork.domain.infrastructure.api.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.model.RoomUpdateInfo
import etude.pintxos.chatwork.domain.infrastructure.api.v0.parser.GetUpdateParser
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.UpdateInfoResponse
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncApi, V0AsyncEntityIO}
import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room.RoomId
import org.json4s.JsonAST.{JField, JObject}
import org.json4s._

import scala.concurrent.Future

object GetUpdate extends V0AsyncEntityIO {

  def updateRaw(updateLastId: Boolean = true)(implicit context: EntityIOContext[Future]): Future[JValue] = {
    implicit val executor = getExecutionContext(context)

    getLastId(context) match {
      case Some(lastId) =>
        V0AsyncApi.api("get_update", Map("last_id" -> lastId)) map {
          json =>
            if (updateLastId) {
              GetUpdateParser.parseLastId(json) match {
                case Some(newLastId) => setLastId(newLastId, context)
                case _ =>
              }
            }
            json
        }
      case _ =>
        throw new IllegalStateException("No last_id found in the context")
    }
  }

  def update(updateLastId: Boolean = true)(implicit context: EntityIOContext[Future]): Future[UpdateInfoResponse] = {
    implicit val executor = getExecutionContext(context)

    updateRaw(updateLastId) map {
      json =>
        UpdateInfoResponse(
          GetUpdateParser.parseRoomUpdateInfo(json)
        )
    }
  }


}
