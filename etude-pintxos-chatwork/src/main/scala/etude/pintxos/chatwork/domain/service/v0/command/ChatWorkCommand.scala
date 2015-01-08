package etude.pintxos.chatwork.domain.service.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.V0AsyncEntityIO
import etude.pintxos.chatwork.domain.service.v0.request.ChatWorkRequest
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

import scala.concurrent.Future
import scala.util.Try

trait ChatWorkCommand[RQ <: ChatWorkRequest, RS <: ChatWorkResponse]
  extends V0AsyncEntityIO {

  def execute(request: RQ)(implicit context: EntityIOContext[Future]): RS
}
