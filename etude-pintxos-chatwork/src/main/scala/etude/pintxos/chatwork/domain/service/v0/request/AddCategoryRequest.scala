package etude.pintxos.chatwork.domain.service.v0.request

import etude.pintxos.chatwork.domain.model.room.RoomId
import etude.pintxos.chatwork.domain.service.v0.ChatWorkIOContext
import etude.pintxos.chatwork.domain.service.v0.command.AddCategory
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

case class AddCategoryRequest(name: String,
                              rooms: List[RoomId])
  extends ChatWorkRequest {

  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse[_] = {
    AddCategory.execute(this)
  }
}
