package semester.service.chatwork.domain.service.request

import semester.service.chatwork.domain.service.ChatWorkIOContext
import semester.service.chatwork.domain.service.command.GetUpdate
import semester.service.chatwork.domain.service.response.GetUpdateResponse

case class GetUpdateRequest(updateLastId: Boolean = true,
                            lastId: Option[String] = None)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): GetUpdateResponse = {
    GetUpdate.execute(this)
  }
}

