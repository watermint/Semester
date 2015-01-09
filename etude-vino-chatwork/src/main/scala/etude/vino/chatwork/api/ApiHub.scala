package etude.vino.chatwork.api

import java.util.concurrent.{ConcurrentLinkedQueue, TimeUnit}

import akka.actor.{Actor, ActorSystem, Props}
import etude.epice.logging.LoggerFactory
import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.ChatWorkEntityIO
import etude.pintxos.chatwork.domain.service.v0.request.ChatWorkRequest
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

case class ApiHub()
  extends ChatWorkEntityIO with Actor {

  private val logger = LoggerFactory.getLogger(getClass)

  private val clockCycleInMillis = 3000

  private val realTimeQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val normalQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val lowQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val lowerQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private implicit val executionContext = ApiHub.system.dispatcher

  case class ApiTick()

  ApiHub.system.scheduler.schedule(
    Duration.create(clockCycleInMillis, TimeUnit.MILLISECONDS),
    Duration.create(clockCycleInMillis, TimeUnit.MILLISECONDS),
    self,
    ApiTick()
  )

  def receive: Receive = {
    case r: ApiTick =>
      execute()

    case r: ApiEnqueue =>
      enqueue(r.request)(r.priority)

    case r: ChatWorkResponse =>
      ApiHub.system.eventStream.publish(r)
  }

  def enqueue(request: ChatWorkRequest)(priority: Priority = PriorityNormal): Unit = {
    logger.info(s"Enqueue request: $request with priority $priority")
    priority match {
      case PriorityRealTime =>
        realTimeQueue.add(request)
        self ! ApiTick()
      case PriorityNormal => normalQueue.add(request)
      case PriorityLow => lowQueue.add(request)
      case PriorityLower => lowerQueue.add(request)
    }
  }

  protected def execute(): Unit = {
    dequeue() match {
      case None => // NOP
      case Some(req) =>
        Api.ref ! req
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

  def props(): Props = Props(ApiHub())
}



