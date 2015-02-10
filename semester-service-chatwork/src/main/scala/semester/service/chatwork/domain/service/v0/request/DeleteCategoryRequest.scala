package semester.service.chatwork.domain.service.v0.request

import semester.service.chatwork.domain.model.room.CategoryId
import semester.service.chatwork.domain.service.v0.ChatWorkIOContext
import semester.service.chatwork.domain.service.v0.command.DeleteCategory
import semester.service.chatwork.domain.service.v0.response.ChatWorkResponse

case class DeleteCategoryRequest(categoryId: CategoryId)
  extends ChatWorkRequest {

  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    DeleteCategory.execute(this)
  }
}
