package etude.vino.chatwork.api

import java.util.concurrent.{ExecutorService, Executors}

import akka.actor._
import akka.pattern.pipe
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.pintxos.chatwork.domain.service.v0.{Api => ChatWorkApi, SessionTimeoutException, NoLastIdAvailableException, NoSessionAvailableException}
import etude.pintxos.chatwork.domain.service.v0.request.{GetUpdateRequest, ChatWorkRequest}
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse
import etude.vino.chatwork.Main

import scala.concurrent.{Future, ExecutionContext}

case class Api() extends Actor {
  val logger = LoggerFactory.getLogger(getClass)

  val executorsPool: ExecutorService = Executors.newSingleThreadExecutor()
  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)
  implicit val entityIOContext = AsyncEntityIOContextOnV0Api.fromThinConfig()

  ChatWorkApi.login(entityIOContext)

  override def supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy(maxNrOfRetries = 3) {
      case _: NoLastIdAvailableException =>
        val response = GetUpdateRequest().execute(entityIOContext)
        self ! response
        SupervisorStrategy.Resume

      case _: NoSessionAvailableException =>
        SupervisorStrategy.Restart

      case _: SessionTimeoutException =>
        SupervisorStrategy.Restart

      case _: Exception =>
        Main.shutdown()
        SupervisorStrategy.Stop
    }
  }

  def receive: Receive = {
    case req: ChatWorkRequest =>
      logger.info(s"Execute request: $req")
      Future {
        req.execute(entityIOContext)
      } pipeTo self

    case res: ChatWorkResponse =>
      Api.system.eventStream.publish(res)
  }
}

object Api {
  val system = ActorSystem("cw-api")

  val ref = system.actorOf(Props[Api])
}
