package etude.pintxos.chatwork.domain.service.v0.response

import etude.pintxos.chatwork.domain.model.room.{Participant, Room}
import etude.pintxos.chatwork.domain.service.v0.request.GetRoomInfoRequest
import org.json4s.JValue

case class GetRoomInfoResponse(rawResponse: JValue,
                               request: GetRoomInfoRequest,
                               room: Room,
                               participant: Participant)
  extends ChatWorkResponse {
  type Request = GetRoomInfoRequest
}
