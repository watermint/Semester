package etude.vino.chatwork.service.api

import java.io.IOException
import java.util.concurrent.{ConcurrentLinkedQueue, Semaphore, TimeUnit, TimeoutException}

import akka.actor._
import etude.epice.logging.LoggerFactory
import etude.epice.utility.qos.TimeoutSemaphore
import etude.pintxos.chatwork.domain.service.v0.request.ChatWorkRequest
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse
import etude.pintxos.chatwork.domain.service.v0.{CommandPermissionException, CommandFailureException, ChatWorkEntityIO, SessionTimeoutException}

import scala.concurrent.duration._

case class ApiHub(clockCycleInSeconds: Int)
  extends ChatWorkEntityIO with Actor {

  private val logger = LoggerFactory.getLogger(getClass)

  private val queues: Map[Priority, ConcurrentLinkedQueue[ChatWorkRequest]] = {
    Priority.priorities.map(p => p -> new ConcurrentLinkedQueue[ChatWorkRequest]()).toMap
  }

  private val semaphore = new TimeoutSemaphore(java.time.Duration.ofSeconds(60))

  private val api = context.actorOf(ApiSession.props())

  private implicit val executionContext = Api.system.dispatcher

  case class ApiTick()

  override def supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy(maxNrOfRetries = 1) {
      case e: SessionTimeoutException =>
        logger.warn("No session available", e)
        semaphore.release()
        SupervisorStrategy.Restart

      case e@(_: java.net.SocketException |
              _: org.apache.http.NoHttpResponseException |
              _: IOException |
              _: TimeoutException) =>

        logger.warn("Issue on network connection", e)
        Api.ensureAvailable()
        semaphore.release()
        Api.system.eventStream.publish(NetworkRecovered())
        logger.info("Trying to resume")
        SupervisorStrategy.Resume

      case e@(_: CommandFailureException |
              _: CommandPermissionException) =>
        logger.warn("Issue on API", e)
        SupervisorStrategy.Resume

      case e: Exception =>
        logger.debug(s"Unexpected exception: Supervisor stops operation", e)
        SupervisorStrategy.Stop
    }
  }

  Api.system.scheduler.schedule(
    Duration.create(clockCycleInSeconds, TimeUnit.SECONDS),
    Duration.create(clockCycleInSeconds, TimeUnit.SECONDS),
    self,
    ApiTick()
  )

  def receive: Receive = {
    case r: NetworkRecovered =>
      semaphore.release()

    case r: ApiTick =>
      execute()

    case r: ApiEnqueue =>
      enqueue(r.request)(r.priority)
      if (r.priority.equals(PriorityP1)) {
        self ! ApiTick()
      }

    case r: ChatWorkResponse =>
      semaphore.release()
  }

  def enqueue(request: ChatWorkRequest)(priority: Priority = PriorityP2): Unit = {
    logger.debug(s"Enqueue request: $request with priority $priority")
    queues(priority).add(request)
  }

  protected def execute(): Unit = {
    logger.debug(s"Execute: $semaphore")
    if (semaphore.tryAcquire()) {
      dequeue() match {
        case None =>
          semaphore.release()

        case Some(req) =>
          api ! req
      }
    }
  }

  protected def dequeue(): Option[ChatWorkRequest] = {
    logger.debug(s"Queue size: ${Priority.priorities.map(p => s"${p.name}: ${queues(p).size()}").mkString(", ")}")
    Priority.priorities.foreach {
      priority =>
        val q = queues(priority)
        if (q.size() > 0) {
          logger.debug(s"Dequeue from priority : $priority")
          return Some(q.poll())
        }
    }
    logger.debug(s"No task found")
    None
  }
}

object ApiHub {
  def props(clockCycleInSeconds: Int): Props = Props(ApiHub(clockCycleInSeconds))
}



