package etude.pintxos.chatwork.domain.service.v0.command

import etude.pintxos.chatwork.domain.service.v0.parser.GetUpdateParser
import etude.pintxos.chatwork.domain.service.v0.request.GetUpdateRequest
import etude.pintxos.chatwork.domain.service.v0.response.GetUpdateResponse
import etude.pintxos.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}

object GetUpdate
  extends ChatWorkCommand[GetUpdateRequest, GetUpdateResponse] {

  def execute(request: GetUpdateRequest)(implicit context: ChatWorkIOContext): GetUpdateResponse = {
    (request.lastId, getLastId(context)) match {
      case (Some(lastId), _) => getUpdate(lastId, request)(context)
      case (_, Some(lastId)) => getUpdate(lastId, request)(context)
      case _ =>
        throw new IllegalStateException("No last_id found in the context")
    }
  }

  private def getUpdate(lastId: String, request: GetUpdateRequest)(implicit context: ChatWorkIOContext): GetUpdateResponse = {
    val json = ChatWorkApi.api("get_update", Map("last_id" -> lastId))
    val newLastId = GetUpdateParser.parseLastId(json)

    newLastId.foreach(setLastId(_, context))

    GetUpdateResponse(
      json,
      request,
      GetUpdateParser.parseRoomUpdateInfo(json),
      newLastId
    )
  }
}
