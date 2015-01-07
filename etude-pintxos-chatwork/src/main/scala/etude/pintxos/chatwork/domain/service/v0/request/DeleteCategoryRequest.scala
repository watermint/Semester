package etude.pintxos.chatwork.domain.service.v0.request

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.command.DeleteCategory
import etude.pintxos.chatwork.domain.service.v0.response.{ChatWorkResponse, DeleteCategoryResponse}
import etude.pintxos.chatwork.domain.model.room.CategoryId

import scala.concurrent.Future

case class DeleteCategoryRequest(categoryId: CategoryId)
  extends ChatWorkRequest {

  def execute(implicit context: EntityIOContext[Future]): Future[ChatWorkResponse] = {
    DeleteCategory.execute(this)
  }
}
