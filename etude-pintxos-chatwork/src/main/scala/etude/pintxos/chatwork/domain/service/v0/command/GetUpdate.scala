package etude.pintxos.chatwork.domain.service.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.Api
import etude.pintxos.chatwork.domain.service.v0.parser.GetUpdateParser
import etude.pintxos.chatwork.domain.service.v0.request.GetUpdateRequest
import etude.pintxos.chatwork.domain.service.v0.response.GetUpdateResponse
import org.json4s._

import scala.concurrent.Future

object GetUpdate
  extends ChatWorkCommand[GetUpdateRequest, GetUpdateResponse] {


  def execute(request: GetUpdateRequest)(implicit context: EntityIOContext[Future]): GetUpdateResponse = {
    implicit val executor = getExecutionContext(context)
    val json = updateRaw(request.updateLastId)

    GetUpdateResponse(
      json,
      GetUpdateParser.parseRoomUpdateInfo(json)
    )
  }

  private def updateRaw(updateLastId: Boolean = true)(implicit context: EntityIOContext[Future]): JValue = {
    implicit val executor = getExecutionContext(context)

    getLastId(context) match {
      case Some(lastId) =>
        val json = Api.api("get_update", Map("last_id" -> lastId))
        if (updateLastId) {
          GetUpdateParser.parseLastId(json) match {
            case Some(newLastId) => setLastId(newLastId, context)
            case _ =>
          }
        }
        json

      case _ =>
        throw new IllegalStateException("No last_id found in the context")
    }
  }

}
