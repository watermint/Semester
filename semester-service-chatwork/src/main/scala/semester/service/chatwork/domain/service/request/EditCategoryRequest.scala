package semester.service.chatwork.domain.service.request

import semester.service.chatwork.domain.model.room.Category
import semester.service.chatwork.domain.service.ChatWorkIOContext
import semester.service.chatwork.domain.service.command.EditCategory
import semester.service.chatwork.domain.service.response.ChatWorkResponse

case class EditCategoryRequest(category: Category)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    EditCategory.execute(this)
  }
}

