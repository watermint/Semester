package semester.application.vino.service.api

import java.net.URI
import java.time.temporal.ChronoUnit
import java.time.{Duration, Instant}
import java.util.concurrent.atomic.AtomicInteger

import akka.actor._
import semester.foundation.http.{Client, Response}
import semester.foundation.logging.LoggerFactory
import semester.service.chatwork.domain.service.ChatWorkIOContext
import semester.service.chatwork.domain.service.request.ChatWorkRequest
import semester.service.chatwork.domain.service.response.ChatWorkResponse

import scala.concurrent.{duration, Await, Future}
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
      val response = Await.result(execute, duration.Duration(responseTimeoutInSeconds, duration.SECONDS))

      self ! response

    case res: ChatWorkResponse =>
      Api.system.eventStream.publish(res)
  }
}

object Api {
  val logger = LoggerFactory.getLogger(Api.getClass)

  val system = ActorSystem("cw-api")

  def props(chatworkContext: ChatWorkIOContext) = Props(Api(chatworkContext))

  def ensureAvailable(): Boolean = ensureAvailable(Duration.ofHours(1))

  def ensureAvailable(maxWait: Duration): Boolean = {
    implicit val executors = Api.system.dispatcher
    val end = Instant.now.plus(maxWait)
    val minimumPeriod = Duration.ofSeconds(5)
    val period = Seq(minimumPeriod, Seq(minimumPeriod, maxWait.dividedBy(10)).max).min
    val client = Client()

    while (Instant.now().isBefore(end)) {
      val ping = Future {
        client.get(new URI("http://www.chatwork.com"))
      }
      try {
        val pingResult: Try[Response] = Await.result(ping, duration.Duration(minimumPeriod.get(ChronoUnit.MILLIS), duration.NANOSECONDS))
        if (pingResult.isSuccess) {
          return true
        } else {
          logger.info(s"Waiting for network become available.")
          Thread.sleep(period.get(ChronoUnit.MILLIS))
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
