package etude.vino.chatwork.api

import java.util.concurrent.{ExecutorService, Executors}

import akka.actor.SupervisorStrategy.Stop
import akka.actor._
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.pintxos.chatwork.domain.service.v0.request.ChatWorkRequest

import scala.concurrent.ExecutionContext

case class Api() extends Actor {
  val logger = LoggerFactory.getLogger(getClass)

  val executorsPool: ExecutorService = Executors.newSingleThreadExecutor()
  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)
  implicit val entityIOContext = AsyncEntityIOContextOnV0Api.fromThinConfig()

  override def supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy() {
      case _: Exception => Stop
    }
  }

  def receive: Receive = {
    case req: ChatWorkRequest =>
      logger.info(s"Execute request: $req")
      val response = req.execute(entityIOContext)
      sender ! response
  }
}

object Api {
  val system = ActorSystem("cw-api")

  val ref = system.actorOf(Props[Api])
}
