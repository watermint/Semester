package etude.pintxos.chatwork.domain.infrastructure.api.v0.request

import etude.pintxos.chatwork.domain.model.room.RoomId

case class GetRoomInfoRequest(roomId: RoomId)
  extends ChatWorkRequest

