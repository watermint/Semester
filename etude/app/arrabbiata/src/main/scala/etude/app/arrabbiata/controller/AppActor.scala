package etude.app.arrabbiata.controller

import akka.actor.{Props, ActorSystem, Actor}
import etude.app.arrabbiata.controller.message.{MessageWithoutSession, MessageWithSession}
import etude.app.arrabbiata.ui.UIActor
import etude.app.arrabbiata.ui.message.composite.NoSession
import java.util.concurrent.{Executors, ExecutorService}
import scala.concurrent.ExecutionContext
import etude.foundation.logging.LoggerFactory

class AppActor extends Actor {
  def noSession(): Unit = {
    UIActor.ui ! NoSession()
  }

  def receive = {
    case e: MessageWithoutSession =>
      AppActor.logger.info(s"message without session: ${e.getClass}")
      e.perform()
    case e: MessageWithSession =>
      AppActor.logger.info(s"message WITH session: ${e.getClass}")
      Session.session.get() match {
        case null =>
          noSession()
        case s =>
          e.perform(s)
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
