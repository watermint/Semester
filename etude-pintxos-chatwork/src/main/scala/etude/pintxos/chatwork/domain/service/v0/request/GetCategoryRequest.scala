package etude.pintxos.chatwork.domain.service.v0.request

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.command.GetCategory
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

import scala.concurrent.Future

case class GetCategoryRequest()
  extends ChatWorkRequest {
  def execute(implicit context: EntityIOContext[Future]): ChatWorkResponse = {
    GetCategory.execute(this)
  }
}

