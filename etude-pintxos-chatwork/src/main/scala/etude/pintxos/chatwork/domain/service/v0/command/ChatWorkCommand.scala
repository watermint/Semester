package etude.pintxos.chatwork.domain.service.v0.command

import etude.pintxos.chatwork.domain.service.v0.request.ChatWorkRequest
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse
import etude.pintxos.chatwork.domain.service.v0.{ChatWorkEntityIO, ChatWorkIOContext}

trait ChatWorkCommand[RQ <: ChatWorkRequest, RS <: ChatWorkResponse]
  extends ChatWorkEntityIO {

  def execute(request: RQ)(implicit context: ChatWorkIOContext): RS
}
