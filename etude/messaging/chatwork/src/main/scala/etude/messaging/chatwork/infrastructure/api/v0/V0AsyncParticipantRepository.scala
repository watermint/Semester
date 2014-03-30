package etude.messaging.chatwork.infrastructure.api.v0

import etude.messaging.chatwork.domain.room.{RoomId, Participant, AsyncParticipantRepository}
import scala.concurrent.Future
import etude.foundation.domain.lifecycle.{ResultWithEntity, EntityIOContext}
import etude.messaging.chatwork.infrastructure.api.NotImplementedException
import etude.foundation.domain.lifecycle.async.AsyncResultWithEntity

class V0AsyncParticipantRepository extends AsyncParticipantRepository with V0EntityIO[Future] {
  type This <: V0AsyncParticipantRepository

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
    Future.failed(NotImplementedException("delete operation is not supported"))
  }
}
