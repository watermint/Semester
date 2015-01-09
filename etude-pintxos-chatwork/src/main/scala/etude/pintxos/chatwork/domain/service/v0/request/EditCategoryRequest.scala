package etude.pintxos.chatwork.domain.service.v0.request

import etude.pintxos.chatwork.domain.model.room.Category
import etude.pintxos.chatwork.domain.service.v0.ChatWorkIOContext
import etude.pintxos.chatwork.domain.service.v0.command.EditCategory
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

case class EditCategoryRequest(category: Category)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    EditCategory.execute(this)
  }
}

