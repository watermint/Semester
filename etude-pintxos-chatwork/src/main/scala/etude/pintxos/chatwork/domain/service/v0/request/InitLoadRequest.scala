package etude.pintxos.chatwork.domain.service.v0.request

import etude.pintxos.chatwork.domain.service.v0.ChatWorkIOContext
import etude.pintxos.chatwork.domain.service.v0.command.InitLoad
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

case class InitLoadRequest()
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    InitLoad.execute(this)
  }
}
