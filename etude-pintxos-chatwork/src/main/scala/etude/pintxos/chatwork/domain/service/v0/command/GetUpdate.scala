package etude.pintxos.chatwork.domain.service.v0.command

import etude.pintxos.chatwork.domain.service.v0.parser.GetUpdateParser
import etude.pintxos.chatwork.domain.service.v0.request.GetUpdateRequest
import etude.pintxos.chatwork.domain.service.v0.response.GetUpdateResponse
import etude.pintxos.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}
import org.json4s._

object GetUpdate
  extends ChatWorkCommand[GetUpdateRequest, GetUpdateResponse] {


  def execute(request: GetUpdateRequest)(implicit context: ChatWorkIOContext): GetUpdateResponse = {
    val json = updateRaw(request.updateLastId)

    GetUpdateResponse(
      json,
      GetUpdateParser.parseRoomUpdateInfo(json)
    )
  }

  private def updateRaw(updateLastId: Boolean = true)(implicit context: ChatWorkIOContext): JValue = {
    getLastId(context) match {
      case Some(lastId) =>
        val json = ChatWorkApi.api("get_update", Map("last_id" -> lastId))
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
