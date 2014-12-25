package etude.pintxos.chatwork.domain.infrastructure.api.v0.request

import etude.pintxos.chatwork.domain.model.room.Participant

case class UpdateRoomRequest(participant: Participant)
  extends ChatWorkRequest
