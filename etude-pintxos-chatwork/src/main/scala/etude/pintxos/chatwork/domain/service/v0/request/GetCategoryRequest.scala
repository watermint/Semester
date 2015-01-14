package etude.pintxos.chatwork.domain.service.v0.request

import etude.pintxos.chatwork.domain.service.v0.ChatWorkIOContext
import etude.pintxos.chatwork.domain.service.v0.command.GetCategory
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

case class GetCategoryRequest()
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse[_] = {
    GetCategory.execute(this)
  }
}

