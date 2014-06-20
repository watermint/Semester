package etude.app.arrabbiata.controller

import java.util.concurrent.{ExecutorService, Executors}

import akka.actor.{Actor, ActorSystem, Props}
import etude.app.arrabbiata.controller.message.{CallbackMessage, Message, MessageWithSession, MessageWithoutSession}
import etude.app.arrabbiata.state.Session
import etude.app.arrabbiata.ui.UIActor
import etude.app.arrabbiata.ui.message.composite.NoSession
import etude.foundation.logging.LoggerFactory

import scala.concurrent.ExecutionContext

class AppActor extends Actor {
  def noSession(): Unit = {
    UIActor.ui ! NoSession()
  }

  def callback(m: Message) = {
    m match {
      case c: CallbackMessage =>
        UIActor.ui ! c.uiMessage
      case _ =>
    }
  }

  def receive = {
    case e: MessageWithoutSession =>
      AppActor.logger.info(s"message without session: ${e.getClass}")
      e.perform()
      callback(e)
    case e: MessageWithSession =>
      AppActor.logger.info(s"message WITH session: ${e.getClass}")
      Session.session.get() match {
        case null =>
          noSession()
        case s =>
          e.perform(s)
          callback(e)
      }
  }
}

object AppActor {
  val logger = LoggerFactory.getLogger(getClass)

  val system = ActorSystem("session")

  val app = system.actorOf(Props[AppActor], name = "session")

  val executorsPool: ExecutorService = Executors.newCachedThreadPool()

  val executionContext = ExecutionContext.fromExecutorService(executorsPool)
}
