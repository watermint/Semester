package etude.vino.chatwork.api

import akka.actor._
import akka.pattern.pipe
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.service.v0._
import etude.pintxos.chatwork.domain.service.v0.request.{ChatWorkRequest, GetUpdateRequest}
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse
import etude.vino.chatwork.Main

import scala.concurrent.Future

case class Api() extends Actor {
  val logger = LoggerFactory.getLogger(getClass)

  implicit val executors = Api.system.dispatcher
  implicit val entityIOContext = ChatWorkIOContext.fromThinConfig()

  ChatWorkApi.login(entityIOContext)

  override def supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy(maxNrOfRetries = 0) {
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
