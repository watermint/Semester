package etude.pintxos.chatwork.domain.service.v0.response

import etude.pintxos.chatwork.domain.service.v0.model.RoomUpdateInfo
import etude.pintxos.chatwork.domain.service.v0.request.GetUpdateRequest
import org.json4s.JValue

case class GetUpdateResponse(rawResponse: JValue,
                             request: GetUpdateRequest,
                             roomUpdateInfo: Seq[RoomUpdateInfo],
                             lastId: Option[String])
  extends ChatWorkResponse {
  type Request = GetUpdateRequest
}
