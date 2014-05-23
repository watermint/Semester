package etude.app.arrabbiata.controller

import etude.domain.core.lifecycle.async.AsyncEntityIOContext
import etude.messaging.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import java.util.concurrent.{Executors, ExecutorService}
import scala.concurrent.{Future, ExecutionContext}
import etude.messaging.chatwork.domain.lifecycle.message.AsyncMessageRepository
import etude.messaging.chatwork.domain.lifecycle.room.{AsyncRoomRepository, AsyncParticipantRepository}
import etude.messaging.chatwork.domain.lifecycle.account.AsyncAccountRepository
import java.util.concurrent.atomic.AtomicReference
import etude.messaging.chatwork.domain.model.room.Room

case class Session(username: String,
                   password: String,
                   orgId: Option[String] = None) {

  implicit val executionContext = AppActor.executionContext

  implicit val ioContext: AsyncEntityIOContext = orgId match {
    case Some(o) => AsyncEntityIOContextOnV0Api(o, username, password)
    case _ => AsyncEntityIOContextOnV0Api(username, password)
  }

  implicit val accountRepository: AsyncAccountRepository = AsyncAccountRepository.ofContext(ioContext)

  implicit val messageRepository: AsyncMessageRepository = AsyncMessageRepository.ofContext(ioContext)

  implicit val participantRepository: AsyncParticipantRepository = AsyncParticipantRepository.ofContext(ioContext)

  implicit val roomRepository: AsyncRoomRepository = AsyncRoomRepository.ofContext(ioContext)

  def myRoom(): Future[Room] = {
    roomRepository.myRoom()
  }
}

object Session {
  val session: AtomicReference[Session] = new AtomicReference[Session]

  def login(username: String, password: String, orgId: String): Future[Session] = {
    implicit val executionContext = AppActor.executionContext

    val s = orgId match {
      case null => Session(username, password)
      case o if "".equals(o.trim) => Session(username, password)
      case o => Session(username, password, Some(o))
    }

    s.myRoom() map {
      r =>
        session.set(s)
        s
    }
  }
}
