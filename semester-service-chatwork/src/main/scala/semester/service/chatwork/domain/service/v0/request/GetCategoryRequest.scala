package semester.service.chatwork.domain.service.v0.request

import semester.service.chatwork.domain.service.v0.ChatWorkIOContext
import semester.service.chatwork.domain.service.v0.command.GetCategory
import semester.service.chatwork.domain.service.v0.response.ChatWorkResponse

case class GetCategoryRequest()
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    GetCategory.execute(this)
  }
}

