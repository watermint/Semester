package etude.vino.chatwork.api

import java.util.concurrent.{ExecutorService, Executors}

import akka.actor._
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.pintxos.chatwork.domain.service.v0.{Api => ChatWorkApi, NoLastIdAvailableException, NoSessionAvailableException}
import etude.pintxos.chatwork.domain.service.v0.request.{GetUpdateRequest, ChatWorkRequest}
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

import scala.concurrent.ExecutionContext

case class Api() extends Actor {
  val logger = LoggerFactory.getLogger(getClass)

  val executorsPool: ExecutorService = Executors.newSingleThreadExecutor()
  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)
  implicit val entityIOContext = AsyncEntityIOContextOnV0Api.fromThinConfig()

  ChatWorkApi.login(entityIOContext)

  override def supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy(maxNrOfRetries = 3) {
      case _: NoSessionAvailableException =>
        ChatWorkApi.login(entityIOContext)
        SupervisorStrategy.Resume

      case _: NoLastIdAvailableException =>
        val response = GetUpdateRequest().execute(entityIOContext)
        self ! response
        SupervisorStrategy.Resume

      case _: Exception =>
        SupervisorStrategy.Stop
    }
  }

  def receive: Receive = {
    case req: ChatWorkRequest =>
      logger.info(s"Execute request: $req")
      val response = req.execute(entityIOContext)
      Api.system.eventStream.publish(response)

    case res: ChatWorkResponse =>
      Api.system.eventStream.publish(res)
  }
}

object Api {
  val system = ActorSystem("cw-api")

  val ref = system.actorOf(Props[Api])
}
