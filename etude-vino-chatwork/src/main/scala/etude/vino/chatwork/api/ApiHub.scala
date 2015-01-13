package etude.vino.chatwork.api

import java.util.concurrent.{Semaphore, ConcurrentLinkedQueue, TimeUnit}

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.service.v0.ChatWorkEntityIO
import etude.pintxos.chatwork.domain.service.v0.request.ChatWorkRequest
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

import scala.concurrent.duration._

case class ApiHub(api: ActorRef, clockCycleInMillis: Int)
  extends ChatWorkEntityIO with Actor {

  private val logger = LoggerFactory.getLogger(getClass)

  private val realTimeQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val normalQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val lowQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val lowerQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val semaphore = new Semaphore(1)

  private implicit val executionContext = ApiHub.system.dispatcher

  case class ApiTick()

  schedule()

  def schedule(): Unit = {
    ApiHub.system.scheduler.scheduleOnce(
      Duration.create(clockCycleInMillis, TimeUnit.MILLISECONDS),
      self,
      ApiTick()
    )
  }

  def receive: Receive = {
    case r: ApiTick =>
      if (semaphore.tryAcquire()) {
        execute()
      }

    case r: ApiEnqueue =>
      enqueue(r.request)(r.priority)

    case r: ChatWorkResponse =>
      semaphore.release()
      ApiHub.system.eventStream.publish(r)
      schedule()
  }

  def enqueue(request: ChatWorkRequest)(priority: Priority = PriorityNormal): Unit = {
    logger.info(s"Enqueue request: $request with priority $priority")
    priority match {
      case PriorityRealTime => realTimeQueue.add(request)
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
    logger.info(s"Queue size: Realtime: ${realTimeQueue.size()}, Normal: ${normalQueue.size()}, Low: ${lowQueue.size()}, Lower: ${lowerQueue.size()}")
    realTimeQueue.poll() match {
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
  val system = ActorSystem("cw-apihub")

  def props(api: ActorRef, clockCycleInMillis: Int): Props = Props(ApiHub(api, clockCycleInMillis))
}



