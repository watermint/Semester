package etude.pintxos.chatwork.domain.service.v0.request

import etude.pintxos.chatwork.domain.model.room.CategoryId
import etude.pintxos.chatwork.domain.service.v0.ChatWorkIOContext
import etude.pintxos.chatwork.domain.service.v0.command.DeleteCategory
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

case class DeleteCategoryRequest(categoryId: CategoryId)
  extends ChatWorkRequest {

  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    DeleteCategory.execute(this)
  }
}
