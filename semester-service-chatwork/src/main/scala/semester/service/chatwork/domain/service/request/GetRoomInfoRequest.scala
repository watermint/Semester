package semester.service.chatwork.domain.service.request

import semester.service.chatwork.domain.model.room.RoomId
import semester.service.chatwork.domain.service.ChatWorkIOContext
import semester.service.chatwork.domain.service.command.GetRoomInfo
import semester.service.chatwork.domain.service.response.ChatWorkResponse

case class GetRoomInfoRequest(roomId: RoomId)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    GetRoomInfo.execute(this)
  }
}

