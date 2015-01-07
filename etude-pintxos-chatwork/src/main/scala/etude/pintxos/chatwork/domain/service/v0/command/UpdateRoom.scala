package etude.pintxos.chatwork.domain.service.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.request.UpdateRoomRequest
import etude.pintxos.chatwork.domain.service.v0.response.UpdateRoomResponse
import etude.pintxos.chatwork.domain.service.v0.{V0AsyncApi, V0AsyncEntityIO}
import etude.pintxos.chatwork.domain.model.room.Participant
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

import scala.concurrent.Future

object UpdateRoom
  extends ChatWorkCommand[UpdateRoomRequest, UpdateRoomResponse] {

  def execute(request: UpdateRoomRequest)(implicit context: EntityIOContext[Future]): Future[UpdateRoomResponse] = {
    implicit val executor = getExecutionContext(context)

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

    V0AsyncApi.api(
      "update_room",
      Map(),
      Map(
        "pdata" -> compact(render(pdata))
      )
    ) map {
      json =>
        //TODO: parse json
        UpdateRoomResponse(
          json,
          request.participant
        )
    }
  }
}
