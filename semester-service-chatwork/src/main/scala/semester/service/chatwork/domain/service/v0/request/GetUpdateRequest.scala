package semester.service.chatwork.domain.service.v0.request

import semester.service.chatwork.domain.service.v0.ChatWorkIOContext
import semester.service.chatwork.domain.service.v0.command.GetUpdate
import semester.service.chatwork.domain.service.v0.response.GetUpdateResponse

case class GetUpdateRequest(updateLastId: Boolean = true,
                            lastId: Option[String] = None)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): GetUpdateResponse = {
    GetUpdate.execute(this)
  }
}

