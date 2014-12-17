package etude.pintxos.chatwork.domain.infrastructure.api.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncApi, V0AsyncEntityIO}
import etude.pintxos.chatwork.domain.model.room.Participant
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

import scala.concurrent.Future

object UpdateRoom
  extends V0AsyncEntityIO {

  def updateParticipants(participant: Participant)(implicit context: EntityIOContext[Future]): Future[Participant] = {
    implicit val executor = getExecutionContext(context)

    val admin = participant.admin.map {
      _.value.toString() -> "admin"
    }
    val member = participant.member.map {
      _.value.toString() -> "member"
    }
    val readonly = participant.readonly.map {
      _.value.toString() -> "readonly"
    }

    val pdata = ("room_id" -> participant.identity.value.toString()) ~
      ("role" -> (admin ++ member ++ readonly).toMap)

    V0AsyncApi.api(
      "update_room",
      Map(),
      Map(
        "pdata" -> compact(render(pdata))
      )
    ) map {
      p =>
        //TODO: parse json
        participant
    }
  }
}
