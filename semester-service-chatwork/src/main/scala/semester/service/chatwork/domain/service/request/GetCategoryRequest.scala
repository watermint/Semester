package semester.service.chatwork.domain.service.request

import semester.service.chatwork.domain.service.ChatWorkIOContext
import semester.service.chatwork.domain.service.command.GetCategory
import semester.service.chatwork.domain.service.response.ChatWorkResponse

case class GetCategoryRequest()
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    GetCategory.execute(this)
  }
}

