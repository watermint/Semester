package etude.pintxos.chatwork.domain.service.v0.request

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.command.EditCategory
import etude.pintxos.chatwork.domain.service.v0.response.{ChatWorkResponse, EditCategoryResponse}
import etude.pintxos.chatwork.domain.model.room.Category

import scala.concurrent.Future

case class EditCategoryRequest(category: Category)
  extends ChatWorkRequest {
  def execute(implicit context: EntityIOContext[Future]): ChatWorkResponse = {
    EditCategory.execute(this)
  }
}

