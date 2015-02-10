package semester.service.chatwork.domain.service.v0.command

import semester.service.chatwork.domain.service.v0.request.ChatWorkRequest
import semester.service.chatwork.domain.service.v0.response.ChatWorkResponse
import semester.service.chatwork.domain.service.v0.{ChatWorkEntityIO, ChatWorkIOContext}

trait ChatWorkCommand[RQ <: ChatWorkRequest, RS <: ChatWorkResponse]
  extends ChatWorkEntityIO {

  def execute(request: RQ)(implicit context: ChatWorkIOContext): RS
}
