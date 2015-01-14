package etude.pintxos.chatwork.domain.service.v0.request

import etude.pintxos.chatwork.domain.service.v0.ChatWorkIOContext
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

trait ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse[_]
}