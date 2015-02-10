package semester.service.chatwork.domain.service.request

import semester.service.chatwork.domain.service.ChatWorkIOContext
import semester.service.chatwork.domain.service.response.ChatWorkResponse

trait ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse
}