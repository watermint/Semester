package etude.messaging.chatwork.domain.lifecycle.room

import etude.domain.core.lifecycle.EntityIOContext
import etude.domain.core.lifecycle.async.AsyncRepository
import etude.messaging.chatwork.domain.infrastructure.api.{AsyncEntityIOContextOnV0Api, AsyncEntityIOContextOnV1Api}
import etude.messaging.chatwork.domain.model.room.{Participant, RoomId}

import scala.concurrent.Future

trait AsyncParticipantRepository
  extends ParticipantRepository[Future]
  with AsyncRepository[RoomId, Participant] {

  type This <: AsyncParticipantRepository
}

object AsyncParticipantRepository {
  def ofContext(context: EntityIOContext[Future]): AsyncParticipantRepository = {
    context match {
      case c: AsyncEntityIOContextOnV0Api => ofV0Api()
      case c: AsyncEntityIOContextOnV1Api => ofV1Api()
      case _ => throw new IllegalArgumentException("Unsupported EntityIOContext")
    }
  }

  private def ofV0Api(): AsyncParticipantRepository =
    new AsyncParticipantRepositoryOnV0Api

  private def ofV1Api(): AsyncParticipantRepository =
    new AsyncParticipantRepositoryOnV1Api
}