package etude.pintxos.chatwork.domain.service.v0.command

import etude.pintxos.chatwork.domain.service.v0.request.UpdateRoomRequest
import etude.pintxos.chatwork.domain.service.v0.response.UpdateRoomResponse
import etude.pintxos.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

object UpdateRoom
  extends ChatWorkCommand[UpdateRoomRequest, UpdateRoomResponse] {

  def execute(request: UpdateRoomRequest)(implicit context: ChatWorkIOContext): UpdateRoomResponse = {
    val admin = request.participant.admin.map {
      _.value.toString() -> "admin"
    }
    val member = request.participant.member.map {
      _.value.toString() -> "member"
    }
    val readonly = request.participant.readonly.map {
      _.value.toString() -> "readonly"
    }

    val pdata = ("room_id" -> request.participant.identity.value.toString()) ~
      ("role" -> (admin ++ member ++ readonly).toMap)

    val json = ChatWorkApi.api(
      "update_room",
      Map(),
      Map(
        "pdata" -> compact(render(pdata))
      )
    )
    //TODO: parse json
    UpdateRoomResponse(
      json,
      request.participant
    )

  }
}
