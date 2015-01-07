package etude.pintxos.chatwork.domain.service.v0.response

import etude.pintxos.chatwork.domain.model.room.{Participant, Room}
import org.json4s.JValue

case class GetRoomInfoResponse(rawResponse: JValue,
                               room: Room,
                               participant: Participant)
  extends ChatWorkResponse
