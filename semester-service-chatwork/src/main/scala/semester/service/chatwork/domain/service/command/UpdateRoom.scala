package semester.service.chatwork.domain.service.command

import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import semester.service.chatwork.domain.service.request.UpdateRoomRequest
import semester.service.chatwork.domain.service.response.UpdateRoomResponse
import semester.service.chatwork.domain.service.{ChatWorkApi, ChatWorkIOContext}

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
      request,
      request.participant
    )

  }
}
