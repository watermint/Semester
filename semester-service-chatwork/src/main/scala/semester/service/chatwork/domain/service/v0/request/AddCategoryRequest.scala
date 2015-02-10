package semester.service.chatwork.domain.service.v0.request

import semester.service.chatwork.domain.model.room.RoomId
import semester.service.chatwork.domain.service.v0.ChatWorkIOContext
import semester.service.chatwork.domain.service.v0.command.AddCategory
import semester.service.chatwork.domain.service.v0.response.ChatWorkResponse

case class AddCategoryRequest(name: String,
                              rooms: List[RoomId])
  extends ChatWorkRequest {

  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    AddCategory.execute(this)
  }
}
