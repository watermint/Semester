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

  override val supervisorStrategy: SupervisorStrategy = {
    AllForOneStrategy(maxNrOfRetries = 1) {
      case _: NoSessionAvailableException =>
        SupervisorStrategy.Escalate

      case _: SessionTimeoutException =>
        SupervisorStrategy.Escalate

      case _: Exception =>
        SupervisorStrategy.Restart
    }
  }


  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    ensureAvailable(10000)
  }

  def ensureAvailable(maxWaitInMillis: Int): Boolean = {
    val end = Instant.now.plusMillis(maxWaitInMillis)
    val period = Math.max(1, maxWaitInMillis / 10)

    while (Instant.now().isBefore(end)) {
      try {
        InetAddress.getByName("api.chatwork.com")
        return true
      } catch {
        case _: Exception =>
          Thread.sleep(period)
      }
    }
    logger.info(s"Network unreachable..")
    false
  }

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
