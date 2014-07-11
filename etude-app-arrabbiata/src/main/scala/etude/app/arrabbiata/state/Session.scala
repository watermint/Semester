package etude.app.arrabbiata.state

import java.util.concurrent.atomic.AtomicReference

import etude.app.arrabbiata.controller.AppActor
import etude.domain.core.lifecycle.async.AsyncEntityIOContext
import etude.foundation.logging.LoggerFactory
import etude.adapter.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.adapter.chatwork.domain.lifecycle.account.AsyncAccountRepository
import etude.adapter.chatwork.domain.lifecycle.message.AsyncMessageRepository
import etude.adapter.chatwork.domain.lifecycle.room.{AsyncParticipantRepository, AsyncRoomRepository}
import etude.adapter.chatwork.domain.model.room.Room

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
  val logger = LoggerFactory.getLogger(getClass)

  def fromThinConfig(): Future[Session] = {
    AppActor.loginTryingLock.acquire()
    try {
      implicit val executionContext = AppActor.executionContext
      implicit val s = IOContextSession(AsyncEntityIOContextOnV0Api.fromThinConfig())
      implicit val ioContext = s.ioContext
      val roomRepo = AsyncRoomRepository.ofContext(ioContext)

      roomRepo.myRoom() map {
        r =>
          session.set(s)
          logger.info("Logged in by thin config")
          AppActor.loginTryingLock.release()
          s
      }
    } catch {
      case t: Throwable =>
        AppActor.loginTryingLock.release()
        Future.failed(t)
    }
  }

  def login(username: String, password: String, orgId: String): Future[Session] = {
    implicit val executionContext = AppActor.executionContext
    AppActor.loginTryingLock.acquire()

    val s = orgId match {
      case null => UserPasswordSession(username, password)
      case o if "".equals(o.trim) => UserPasswordSession(username, password)
      case o => UserPasswordSession(username, password, Some(o))
    }

    s.myRoom() map {
      r =>
        session.set(s)
        AppActor.loginTryingLock.release()
        s
    }
  }
}
