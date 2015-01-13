package etude.vino.chatwork.api

import java.net.URI
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger

import akka.actor._
import etude.epice.http.Client
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.service.v0._
import etude.pintxos.chatwork.domain.service.v0.request.ChatWorkRequest
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

import scala.util.{Failure, Success}

case class Api(chatworkContext: ChatWorkIOContext) extends Actor {
  val logger = LoggerFactory.getLogger(getClass)

  implicit val executors = Api.system.dispatcher
  val counter = new AtomicInteger()

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    Api.ensureAvailable()
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
  val logger = LoggerFactory.getLogger(Api.getClass)

  val system = ActorSystem("cw-api")

  def props(chatworkContext: ChatWorkIOContext) = Props(Api(chatworkContext))

  def ensureAvailable(): Boolean = ensureAvailable(3600000) // 1 hour

  def ensureAvailable(maxWaitInMillis: Int): Boolean = {
    val end = Instant.now.plusMillis(maxWaitInMillis)
    val minimumPeriod = 5000
    val period = Math.min(minimumPeriod, Math.max(minimumPeriod, maxWaitInMillis / 10))
    val client = Client()

    while (Instant.now().isBefore(end)) {
      try {
        client.get(new URI("http://www.chatwork.com")) match {
          case Success(res) =>
            return true
          case Failure(_) =>
            logger.info("Waiting for network become available..")
            Thread.sleep(period)
        }
      } catch {
        case _: Exception =>
      }
    }
    logger.info(s"Network unreachable..")
    false
  }
}
