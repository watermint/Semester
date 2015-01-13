package etude.vino.chatwork.api

import java.net.InetAddress
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger

import akka.actor._
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.service.v0._
import etude.pintxos.chatwork.domain.service.v0.request.{ChatWorkRequest, InitLoadRequest}
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

case class Api(chatworkContext: ChatWorkIOContext) extends Actor {
  val logger = LoggerFactory.getLogger(getClass)

  implicit val executors = Api.system.dispatcher
  val counter = new AtomicInteger()
  val chatwork = InetAddress.getByName("www.chatwork.com")

  override val supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy(maxNrOfRetries = 1) {
      case _: NoSessionAvailableException =>
        SupervisorStrategy.Escalate

      case _: SessionTimeoutException =>
        SupervisorStrategy.Escalate

      case _: Exception =>
        SupervisorStrategy.Restart
    }
  }

  def ensureAvailable(maxWaitInMillis: Int): Boolean = {
    val pingCount = new AtomicInteger()
    val end = Instant.now.plusMillis(maxWaitInMillis)

    while (Instant.now().isBefore(end)) {
      try {
        if (chatwork.isReachable(maxWaitInMillis)) {
          return true
        }
        logger.info(s"${pingCount.incrementAndGet()}: Network unreachable..")
        Thread.sleep(maxWaitInMillis / 10)
      } catch {
        case _: Exception =>
          // ignore
      }
    }
    false
  }

  ensureAvailable(10000)

  def receive: Receive = {
    case req: ChatWorkRequest =>
      logger.info(s"Execute request[${counter.incrementAndGet()}]: $req")
      val res = req.execute(chatworkContext)
      self ! res

    case res: ChatWorkResponse =>
      Api.system.eventStream.publish(res)
  }
}

object Api {
  val system = ActorSystem("cw-api")

  def props(chatworkContext: ChatWorkIOContext) = Props(Api(chatworkContext))
}
