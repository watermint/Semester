package semester.service.chatwork.domain.service.request

import semester.service.chatwork.domain.service.ChatWorkIOContext
import semester.service.chatwork.domain.service.command.InitLoad
import semester.service.chatwork.domain.service.response.ChatWorkResponse

case class InitLoadRequest()
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    InitLoad.execute(this)
  }
}
