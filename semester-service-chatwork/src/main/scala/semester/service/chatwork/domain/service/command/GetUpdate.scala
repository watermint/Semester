package semester.service.chatwork.domain.service.command

import semester.service.chatwork.domain.service.parser.GetUpdateParser
import semester.service.chatwork.domain.service.request.GetUpdateRequest
import semester.service.chatwork.domain.service.response.GetUpdateResponse
import semester.service.chatwork.domain.service.{ChatWorkApi, ChatWorkIOContext}

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
