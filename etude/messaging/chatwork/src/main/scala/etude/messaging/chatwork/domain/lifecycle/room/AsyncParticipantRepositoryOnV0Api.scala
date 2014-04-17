package etude.messaging.chatwork.domain.lifecycle.room

import etude.messaging.chatwork.domain.model.room.{RoomId, Participant}
import scala.concurrent.Future
import etude.foundation.domain.lifecycle.{ResultWithEntity, EntityIOContext}
import etude.foundation.domain.lifecycle.async.AsyncResultWithEntity
import etude.messaging.chatwork.domain.infrastructure.api.v0.{V0AsyncApi, V0AsyncInitLoad, V0AsyncEntityIO}

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
    V0AsyncInitLoad.initLoad() map {
      p =>
        p.participants.find(_.roomId.equals(identity)).last
    }
  }

  def store(entity: Participant)(implicit context: EntityIOContext[Future]): Future[ResultWithEntity[This, RoomId, Participant, Future]] = {
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
        AsyncResultWithEntity(this.asInstanceOf[This], entity)
    }
  }

  def deleteByIdentity(identity: RoomId)(implicit context: EntityIOContext[Future]): Future[ResultWithEntity[This, RoomId, Participant, Future]] = {
    Future.failed(new UnsupportedOperationException("delete operation is not supported"))
  }
}
