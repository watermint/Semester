package etude.pintxos.chatwork.domain.service.v0.request

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.command.GetRoomInfo
import etude.pintxos.chatwork.domain.service.v0.response.{ChatWorkResponse, GetRoomInfoResponse}
import etude.pintxos.chatwork.domain.model.room.RoomId

import scala.concurrent.Future

case class GetRoomInfoRequest(roomId: RoomId)
  extends ChatWorkRequest {
  def execute(implicit context: EntityIOContext[Future]): Future[ChatWorkResponse] = {
    GetRoomInfo.execute(this)
  }
}

