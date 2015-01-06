package etude.pintxos.chatwork.domain.infrastructure.api.v0.request

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.ChatWorkResponse

import scala.concurrent.Future

trait ChatWorkRequest {
  def execute(implicit context: EntityIOContext[Future]): Future[ChatWorkResponse]
}