package etude.vino.chatwork.service.api

import java.net.URI
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger

import akka.actor._
import etude.epice.http.{Client, Response}
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.service.v0._
import etude.pintxos.chatwork.domain.service.v0.request.ChatWorkRequest
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Try

case class Api(chatworkContext: ChatWorkIOContext) extends Actor {
  val logger = LoggerFactory.getLogger(getClass)

  implicit val executors = Api.system.dispatcher
  val counter = new AtomicInteger()

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    Api.ensureAvailable()
  }

  val responseTimeoutInSeconds = 20

  def receive: Receive = {
    case req: ChatWorkRequest =>
      logger.debug(s"Execute request[${counter.incrementAndGet()}]: $req")
      Api.system.eventStream.publish(ApiHistory(Instant.now, req))
      val execute: Future[ChatWorkResponse] = Future {
        req.execute(chatworkContext)
      }
      val response = Await.result(execute, responseTimeoutInSeconds seconds)

      self ! response

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
    import scala.concurrent.duration._
    implicit val executors = Api.system.dispatcher
    val end = Instant.now.plusMillis(maxWaitInMillis)
    val minimumPeriod = 5000
    val period = Math.min(minimumPeriod, Math.max(minimumPeriod, maxWaitInMillis / 10))
    val client = Client()

    while (Instant.now().isBefore(end)) {
      val ping = Future {
        client.get(new URI("http://www.chatwork.com"))
      }
      try {
        val pingResult: Try[Response] = Await.result(ping, minimumPeriod milliseconds)
        if (pingResult.isSuccess) {
          return true
        } else {
          logger.info(s"Waiting for network become available.")
          Thread.sleep(period)
        }
      } catch {
        case _: Exception =>
        // NOP
      }
    }
    logger.info(s"Network unreachable..")
    false
  }
}
