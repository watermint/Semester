package semester.service.chatwork.domain.service.request

import semester.service.chatwork.domain.model.room.CategoryId
import semester.service.chatwork.domain.service.ChatWorkIOContext
import semester.service.chatwork.domain.service.command.DeleteCategory
import semester.service.chatwork.domain.service.response.ChatWorkResponse

case class DeleteCategoryRequest(categoryId: CategoryId)
  extends ChatWorkRequest {

  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    DeleteCategory.execute(this)
  }
}
