package etude.pintxos.chatwork.domain.service.v0.response

import etude.pintxos.chatwork.domain.service.v0.model.RoomUpdateInfo
import org.json4s.JValue

case class GetUpdateResponse(rawResponse: JValue,
                             roomUpdateInfo: Seq[RoomUpdateInfo])
  extends ChatWorkResponse
