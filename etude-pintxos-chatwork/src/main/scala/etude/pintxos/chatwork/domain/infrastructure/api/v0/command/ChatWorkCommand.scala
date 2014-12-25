package etude.pintxos.chatwork.domain.infrastructure.api.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.V0AsyncEntityIO
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.ChatWorkRequest
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.ChatWorkResponse

import scala.concurrent.Future

trait ChatWorkCommand[RQ <: ChatWorkRequest, RS <: ChatWorkResponse]
  extends V0AsyncEntityIO {

  def execute(request: RQ)(implicit context: EntityIOContext[Future]): Future[RS]
}
