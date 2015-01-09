package etude.pintxos.chatwork.domain.service.v0.command

import etude.pintxos.chatwork.domain.service.v0.parser.{ParticipantParser, RoomParser}
import etude.pintxos.chatwork.domain.service.v0.request.GetRoomInfoRequest
import etude.pintxos.chatwork.domain.service.v0.response.GetRoomInfoResponse
import etude.pintxos.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}

object GetRoomInfo
  extends ChatWorkCommand[GetRoomInfoRequest, GetRoomInfoResponse] {


  def execute(request: GetRoomInfoRequest)(implicit context: ChatWorkIOContext): GetRoomInfoResponse = {
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

    val json = ChatWorkApi.api(
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
