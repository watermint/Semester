package etude.messaging.chatwork.infrastructure.api.v0

import etude.messaging.chatwork.domain.room.{RoomId, Participant, AsyncParticipantRepository}
import scala.concurrent.Future
import etude.foundation.domain.lifecycle.{ResultWithEntity, EntityIOContext}

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

  def deleteByIdentity(identity: RoomId)(implicit context: EntityIOContext[Future]): Future[ResultWithEntity[This, RoomId, Participant, Future]] = ???

  def store(entity: Participant)(implicit context: EntityIOContext[Future]): Future[ResultWithEntity[This, RoomId, Participant, Future]] = ???
}
