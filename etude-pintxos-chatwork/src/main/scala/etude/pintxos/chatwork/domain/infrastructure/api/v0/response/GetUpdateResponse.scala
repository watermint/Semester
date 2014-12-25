package etude.pintxos.chatwork.domain.infrastructure.api.v0.response

import etude.pintxos.chatwork.domain.infrastructure.api.v0.model.RoomUpdateInfo
import org.json4s.JValue

case class GetUpdateResponse(rawResponse: JValue,
                             roomUpdateInfo: Seq[RoomUpdateInfo])
  extends ChatWorkResponse
