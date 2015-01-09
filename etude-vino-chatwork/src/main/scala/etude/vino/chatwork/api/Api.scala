package etude.vino.chatwork.api

import java.util.concurrent.atomic.{AtomicBoolean, AtomicInteger}
import java.util.concurrent.locks.ReentrantLock

import akka.actor._
import akka.pattern.pipe
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.service.v0._
import etude.pintxos.chatwork.domain.service.v0.request.{ChatWorkRequest, GetUpdateRequest}
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse
import etude.vino.chatwork.Main

import scala.concurrent.Future

case class Api(chatworkContext: ChatWorkIOContext) extends Actor {
  val logger = LoggerFactory.getLogger(getClass)

  implicit val executors = Api.system.dispatcher
  val chatworkLock = new ReentrantLock()
  val counter = new AtomicInteger()

  override def supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy() {
      case _: NoLastIdAvailableException =>
        val response = GetUpdateRequest().execute(chatworkContext)
        self ! response
        SupervisorStrategy.Resume

      case _: NoSessionAvailableException =>
        SupervisorStrategy.Restart

      case _: SessionTimeoutException =>
        SupervisorStrategy.Restart

      case _: Exception =>
        SupervisorStrategy.Stop
    }
  }

  def receive: Receive = {
    case req: ChatWorkRequest =>
      logger.info(s"Execute request[${counter.incrementAndGet()}]: $req")
      Future {
        chatworkLock.lock()
        try {
          req.execute(chatworkContext)
        } finally {
          chatworkLock.unlock()
        }
      } pipeTo self

    case res: ChatWorkResponse =>
      Api.system.eventStream.publish(res)
  }
}

object Api {
  val system = ActorSystem("cw-api")

  def props(chatworkContext: ChatWorkIOContext) = Props(Api(chatworkContext))
}
