package semester.service.chatwork.domain.service.v0.request

import semester.service.chatwork.domain.model.room.Category
import semester.service.chatwork.domain.service.v0.ChatWorkIOContext
import semester.service.chatwork.domain.service.v0.command.EditCategory
import semester.service.chatwork.domain.service.v0.response.ChatWorkResponse

case class EditCategoryRequest(category: Category)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    EditCategory.execute(this)
  }
}

