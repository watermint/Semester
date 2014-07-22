package etude.pintxos.chatwork.domain.infrastructure.api.v0

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.model.room.{Participant, Room, RoomId}

import scala.concurrent.Future

object V0AsyncRoom
  extends V0AsyncEntityIO {

  def room(roomId: RoomId)
          (implicit context: EntityIOContext[Future]): Future[(Room, Participant)] = {

    implicit val executor = getExecutionContext(context)
    import org.json4s.JsonDSL._
    import org.json4s.native.JsonMethods._

    val pdata = ("type" -> "") ~
      ("rid" -> roomId.value) ~
      ("t" -> (roomId.value.toString -> "1")) ~
      ("d" -> List(roomId.value)) ~
      ("m" -> List(roomId.value)) ~
      ("p" -> List(roomId.value)) ~
      ("i" ->
        (
          roomId.value.toString() ->
            ("t" -> 0) ~
              ("l" -> 0) ~
              ("u" -> 0) ~
              ("c" -> 0)
          )
        )

    V0AsyncApi.api(
      "get_room_info",
      Map(),
      Map(
        "pdata" -> compact(render(pdata))
      )
    ) map {
      json =>
        (V0AsyncInitLoad.parseRooms(json).last,
          V0AsyncInitLoad.parseParticipants(json).last)
    }
  }
}
