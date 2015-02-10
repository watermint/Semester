package semester.service.chatwork.domain.service.request

import semester.service.chatwork.domain.model.room.RoomId
import semester.service.chatwork.domain.service.ChatWorkIOContext
import semester.service.chatwork.domain.service.command.AddCategory
import semester.service.chatwork.domain.service.response.ChatWorkResponse

case class AddCategoryRequest(name: String,
                              rooms: List[RoomId])
  extends ChatWorkRequest {

  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    AddCategory.execute(this)
  }
}
