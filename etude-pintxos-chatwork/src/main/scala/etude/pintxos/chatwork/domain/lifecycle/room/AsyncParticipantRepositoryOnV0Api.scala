package etude.pintxos.chatwork.domain.lifecycle.room

import etude.manieres.domain.lifecycle.async.AsyncResultWithIdentity
import etude.manieres.domain.lifecycle.{EntityIOContext, ResultWithIdentity}
import etude.pintxos.chatwork.domain.infrastructure.api.v0.command.{UpdateRoom, InitLoad, GetRoomInfo}
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncApi, V0AsyncEntityIO}
import etude.pintxos.chatwork.domain.model.room.{Participant, RoomId}

import scala.concurrent.Future

private[room]
class AsyncParticipantRepositoryOnV0Api
  extends AsyncParticipantRepository
  with V0AsyncEntityIO {

  type This <: AsyncParticipantRepositoryOnV0Api

  def containsByIdentity(identity: RoomId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executor = getExecutionContext(context)
    InitLoad.initLoad() map {
      p =>
        p.participants.exists(_.roomId.equals(identity))
    }
  }

  def resolve(identity: RoomId)(implicit context: EntityIOContext[Future]): Future[Participant] = {
    implicit val executor = getExecutionContext(context)
    InitLoad.initLoad() flatMap {
      p =>
        GetRoomInfo.room(identity) map {
          r =>
            r._2
        }
    }
  }

  def store(entity: Participant)(implicit context: EntityIOContext[Future]): Future[ResultWithIdentity[This, RoomId, Participant, Future]] = {
    implicit val executor = getExecutionContext(context)
    UpdateRoom.updateParticipants(entity) map {
      p =>
        AsyncResultWithIdentity(this.asInstanceOf[This], entity.identity)
    }
  }

  def deleteByIdentity(identity: RoomId)(implicit context: EntityIOContext[Future]): Future[ResultWithIdentity[This, RoomId, Participant, Future]] = {
    Future.failed(new UnsupportedOperationException("delete operation is not supported"))
  }
}
