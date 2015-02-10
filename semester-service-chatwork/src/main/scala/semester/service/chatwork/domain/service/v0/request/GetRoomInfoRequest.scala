package semester.service.chatwork.domain.service.v0.request

import semester.service.chatwork.domain.model.room.RoomId
import semester.service.chatwork.domain.service.v0.ChatWorkIOContext
import semester.service.chatwork.domain.service.v0.command.GetRoomInfo
import semester.service.chatwork.domain.service.v0.response.ChatWorkResponse

case class GetRoomInfoRequest(roomId: RoomId)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    GetRoomInfo.execute(this)
  }
}

