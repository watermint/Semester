package etude.pintxos.chatwork.domain.service.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.V0AsyncApi
import etude.pintxos.chatwork.domain.service.v0.parser.{ParticipantParser, RoomParser}
import etude.pintxos.chatwork.domain.service.v0.request.GetRoomInfoRequest
import etude.pintxos.chatwork.domain.service.v0.response.GetRoomInfoResponse

import scala.concurrent.Future

object GetRoomInfo
  extends ChatWorkCommand[GetRoomInfoRequest, GetRoomInfoResponse] {


  def execute(request: GetRoomInfoRequest)(implicit context: EntityIOContext[Future]): GetRoomInfoResponse = {

    implicit val executor = getExecutionContext(context)
    import org.json4s.JsonDSL._
    import org.json4s.native.JsonMethods._

    val pdata = ("type" -> "") ~
      ("rid" -> request.roomId.value) ~
      ("t" -> (request.roomId.value.toString -> "1")) ~
      ("d" -> List(request.roomId.value)) ~
      ("m" -> List(request.roomId.value)) ~
      ("p" -> List(request.roomId.value)) ~
      ("i" ->
        (
          request.roomId.value.toString() ->
            ("t" -> 0) ~
              ("l" -> 0) ~
              ("u" -> 0) ~
              ("c" -> 0)
          )
        )

    val json = V0AsyncApi.api(
      "get_room_info",
      Map(),
      Map(
        "pdata" -> compact(render(pdata))
      )
    )

    GetRoomInfoResponse(
      json,
      RoomParser.parseRooms(json).last,
      ParticipantParser.parseParticipants(json).last
    )
  }
}
