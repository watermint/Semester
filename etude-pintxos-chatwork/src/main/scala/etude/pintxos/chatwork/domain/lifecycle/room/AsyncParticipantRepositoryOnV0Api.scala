package etude.pintxos.chatwork.domain.lifecycle.room

import etude.manieres.domain.lifecycle.async.AsyncResultWithIdentity
import etude.manieres.domain.lifecycle.{EntityIOContext, ResultWithIdentity}
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncApi, V0AsyncEntityIO, V0AsyncInitLoad, V0AsyncRoom}
import etude.pintxos.chatwork.domain.model.room.{Participant, RoomId}

import scala.concurrent.Future

private[room]
class AsyncParticipantRepositoryOnV0Api
  extends AsyncParticipantRepository
  with V0AsyncEntityIO {

  type This <: AsyncParticipantRepositoryOnV0Api

  def containsByIdentity(identity: RoomId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executor = getExecutionContext(context)
    V0AsyncInitLoad.initLoad() map {
      p =>
        p.participants.exists(_.roomId.equals(identity))
    }
  }

  def resolve(identity: RoomId)(implicit context: EntityIOContext[Future]): Future[Participant] = {
    implicit val executor = getExecutionContext(context)
    V0AsyncInitLoad.initLoad() flatMap {
      p =>
        V0AsyncRoom.room(identity) map {
          r =>
            r._2
        }
    }
  }

  def store(entity: Participant)(implicit context: EntityIOContext[Future]): Future[ResultWithIdentity[This, RoomId, Participant, Future]] = {
    import org.json4s.JsonDSL._
    import org.json4s.native.JsonMethods._

    implicit val executor = getExecutionContext(context)

    val admin = entity.admin.map {_.value.toString() -> "admin"}
    val member = entity.member.map {_.value.toString() -> "member"}
    val readonly = entity.readonly.map {_.value.toString() -> "readonly"}

    val pdata = ("room_id" -> entity.identity.value.toString()) ~
      ("role" -> (admin ++ member ++ readonly).toMap)

    V0AsyncApi.api(
      "update_room",
      Map(),
      Map(
        "pdata" -> compact(render(pdata))
      )
    ) map {
      p =>
        AsyncResultWithIdentity(this.asInstanceOf[This], entity.identity)
    }
  }

  def deleteByIdentity(identity: RoomId)(implicit context: EntityIOContext[Future]): Future[ResultWithIdentity[This, RoomId, Participant, Future]] = {
    Future.failed(new UnsupportedOperationException("delete operation is not supported"))
  }
}
