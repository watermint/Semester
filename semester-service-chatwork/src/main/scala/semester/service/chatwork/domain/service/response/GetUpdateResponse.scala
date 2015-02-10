package semester.service.chatwork.domain.service.response

import semester.service.chatwork.domain.service.model.RoomUpdateInfo
import semester.service.chatwork.domain.service.request.GetUpdateRequest
import org.json4s.JValue

case class GetUpdateResponse(rawResponse: JValue,
                             request: GetUpdateRequest,
                             roomUpdateInfo: Seq[RoomUpdateInfo],
                             lastId: Option[String])
  extends ChatWorkResponse {
  type Request = GetUpdateRequest
}
