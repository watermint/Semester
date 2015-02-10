package semester.service.chatwork.domain.service.response

import semester.service.chatwork.domain.model.room.{Participant, Room}
import semester.service.chatwork.domain.service.request.GetRoomInfoRequest
import org.json4s.JValue

case class GetRoomInfoResponse(rawResponse: JValue,
                               request: GetRoomInfoRequest,
                               room: Room,
                               participant: Participant)
  extends ChatWorkResponse {
  type Request = GetRoomInfoRequest
}
