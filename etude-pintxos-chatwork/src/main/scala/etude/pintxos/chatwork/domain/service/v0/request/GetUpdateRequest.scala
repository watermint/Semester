package etude.pintxos.chatwork.domain.service.v0.request

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.command.GetUpdate
import etude.pintxos.chatwork.domain.service.v0.response.GetUpdateResponse

import scala.concurrent.Future

case class GetUpdateRequest(updateLastId: Boolean = true)
  extends ChatWorkRequest {
  def execute(implicit context: EntityIOContext[Future]): GetUpdateResponse = {
    GetUpdate.execute(this)
  }
}

