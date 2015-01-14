package etude.vino.chatwork.api

import java.util.concurrent.{ConcurrentLinkedQueue, Semaphore, TimeUnit}

import akka.actor._
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.service.v0.request.ChatWorkRequest
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse
import etude.pintxos.chatwork.domain.service.v0.{ChatWorkEntityIO, SessionTimeoutException}
import etude.vino.chatwork.Main

import scala.concurrent.duration._

case class ApiHub(clockCycleInSeconds: Int)
  extends ChatWorkEntityIO with Actor {

  private val logger = LoggerFactory.getLogger(getClass)

  private val highQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val normalQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val lowQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val lowerQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val semaphore = new Semaphore(1)

  private val api = context.actorOf(ApiSession.props())

  private implicit val executionContext = Api.system.dispatcher

  case class ApiTick()

  schedule()

  override def supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy(maxNrOfRetries = 1) {
      case e: SessionTimeoutException =>
        logger.warn("No session available", e)
        semaphore.release()
        SupervisorStrategy.Restart

      case e @ (_ : java.net.SocketException | _ : org.apache.http.NoHttpResponseException) =>
        logger.warn("Issue on network connection", e)
        Api.ensureAvailable()
        semaphore.release()
        SupervisorStrategy.Resume

      case e: Exception =>
        logger.debug(s"Unexpected exception: Supervisor stops operation", e)
        SupervisorStrategy.Stop
    }
  }

  def schedule(): Unit = {
    Api.system.scheduler.scheduleOnce(
      Duration.create(clockCycleInSeconds, TimeUnit.SECONDS),
      self,
      ApiTick()
    )
  }

  def receive: Receive = {
    case r: RefreshSemaphore =>
      semaphore.release()
      schedule()

    case r: ApiTick =>
      if (semaphore.tryAcquire()) {
        execute()
      }

    case r: ApiEnqueue =>
      enqueue(r.request)(r.priority)

    case r: ChatWorkResponse =>
      semaphore.release()
      schedule()
  }

  def enqueue(request: ChatWorkRequest)(priority: Priority = PriorityNormal): Unit = {
    logger.debug(s"Enqueue request: $request with priority $priority")
    priority match {
      case PriorityHigh => highQueue.add(request)
      case PriorityNormal => normalQueue.add(request)
      case PriorityLow => lowQueue.add(request)
      case PriorityLower => lowerQueue.add(request)
    }
  }

  protected def execute(): Unit = {
    dequeue() match {
      case None => // NOP
      case Some(req) =>
        api ! req
    }
  }

  protected def dequeue(): Option[ChatWorkRequest] = {
    logger.debug(s"Queue size: High: ${highQueue.size()}, Normal: ${normalQueue.size()}, Low: ${lowQueue.size()}, Lower: ${lowerQueue.size()}")
    highQueue.poll() match {
      case r: ChatWorkRequest => Some(r)
      case null => dequeueNormal()
    }
  }

  private def dequeueNormal(): Option[ChatWorkRequest] = {
    if (normalQueue.size() > 0) {
      Some(normalQueue.poll())
    } else {
      dequeueLow()
    }
  }

  private def dequeueLow(): Option[ChatWorkRequest] = {
    if (lowQueue.size() > 0) {
      Some(lowQueue.poll())
    } else {
      dequeueLower()
    }
  }

  private def dequeueLower(): Option[ChatWorkRequest] = {
    if (lowerQueue.size() > 0) {
      Some(lowerQueue.poll())
    } else {
      None
    }
  }
}

object ApiHub {
  def props(clockCycleInSeconds: Int): Props = Props(ApiHub(clockCycleInSeconds))
}



