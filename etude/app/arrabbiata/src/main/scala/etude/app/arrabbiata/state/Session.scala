package etude.app.arrabbiata.state

import java.util.concurrent.atomic.AtomicReference

import etude.app.arrabbiata.controller.AppActor
import etude.domain.core.lifecycle.async.AsyncEntityIOContext
import etude.messaging.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.messaging.chatwork.domain.lifecycle.account.AsyncAccountRepository
import etude.messaging.chatwork.domain.lifecycle.message.AsyncMessageRepository
import etude.messaging.chatwork.domain.lifecycle.room.{AsyncParticipantRepository, AsyncRoomRepository}
import etude.messaging.chatwork.domain.model.room.Room

import scala.concurrent.Future

trait Session {
  implicit val ioContext: AsyncEntityIOContext
}

case class UserPasswordSession(username: String,
                               password: String,
                               orgId: Option[String] = None) extends Session {

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

case class IOContextSession(ioContext: AsyncEntityIOContext)
  extends Session

object Session {
  val session: AtomicReference[Session] = new AtomicReference[Session]

  def fromThinConfig(): Future[Session] = {
    implicit val executionContext = AppActor.executionContext

    try {
      val s = IOContextSession(AsyncEntityIOContextOnV0Api.fromThinConfig())
      session.set(s)
      Future.successful(s)
    } catch {
      case t: Throwable =>
        Future.failed(t)
    }
  }

  def login(username: String, password: String, orgId: String): Future[Session] = {
    implicit val executionContext = AppActor.executionContext

    val s = orgId match {
      case null => UserPasswordSession(username, password)
      case o if "".equals(o.trim) => UserPasswordSession(username, password)
      case o => UserPasswordSession(username, password, Some(o))
    }

    s.myRoom() map {
      r =>
        session.set(s)
        s
    }
  }
}
