package semester.service.chatwork.domain.service.v0.response

import semester.service.chatwork.domain.model.room.Participant
import semester.service.chatwork.domain.service.v0.request.UpdateRoomRequest
import org.json4s.JValue

case class UpdateRoomResponse(rawResponse: JValue,
                              request: UpdateRoomRequest,
                              participant: Participant)
  extends ChatWorkResponse {
  type Request = UpdateRoomRequest
}
