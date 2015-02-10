package semester.service.chatwork.domain.service.command

import semester.service.chatwork.domain.service.request.ChatWorkRequest
import semester.service.chatwork.domain.service.response.ChatWorkResponse
import semester.service.chatwork.domain.service.{ChatWorkEntityIO, ChatWorkIOContext}

trait ChatWorkCommand[RQ <: ChatWorkRequest, RS <: ChatWorkResponse]
  extends ChatWorkEntityIO {

  def execute(request: RQ)(implicit context: ChatWorkIOContext): RS
}
