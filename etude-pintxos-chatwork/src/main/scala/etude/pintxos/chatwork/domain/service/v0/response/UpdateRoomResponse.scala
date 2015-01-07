package etude.pintxos.chatwork.domain.service.v0.response

import etude.pintxos.chatwork.domain.model.room.Participant
import org.json4s.JValue

case class UpdateRoomResponse(rawResponse: JValue,
                              participant: Participant)
  extends ChatWorkResponse
