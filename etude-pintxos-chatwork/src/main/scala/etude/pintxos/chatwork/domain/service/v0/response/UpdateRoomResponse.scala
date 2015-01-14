package etude.pintxos.chatwork.domain.service.v0.response

import etude.pintxos.chatwork.domain.model.room.Participant
import etude.pintxos.chatwork.domain.service.v0.request.UpdateRoomRequest
import org.json4s.JValue

case class UpdateRoomResponse(rawResponse: JValue,
                              request: UpdateRoomRequest,
                              participant: Participant)
  extends ChatWorkResponse[UpdateRoomRequest]
