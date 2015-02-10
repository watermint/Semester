package semester.service.chatwork.domain.service.v0.request

import semester.service.chatwork.domain.service.v0.ChatWorkIOContext
import semester.service.chatwork.domain.service.v0.command.InitLoad
import semester.service.chatwork.domain.service.v0.response.ChatWorkResponse

case class InitLoadRequest()
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    InitLoad.execute(this)
  }
}
