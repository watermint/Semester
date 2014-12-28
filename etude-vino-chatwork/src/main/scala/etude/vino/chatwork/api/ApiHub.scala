package etude.vino.chatwork.api

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{ConcurrentLinkedQueue, TimeUnit}

import akka.actor.{Actor, ActorSystem, Props}
import etude.epice.logging.LoggerFactory
import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.V0AsyncEntityIO
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.ChatWorkRequest

import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}

case class ApiHub(entityIOContext: EntityIOContext[Future])
  extends V0AsyncEntityIO with Actor {

  private val logger = LoggerFactory.getLogger(getClass)

  private val clockCycleInMillis = 5000

  private val lowToNormalRatio = 5

  private val lowToNormalCount = new AtomicInteger()

  private val realTimeQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val normalQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val lowQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  case class ApiTick()

  implicit val executionContext: ExecutionContext = ApiHub.system.dispatcher

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
  }

  def enqueue(request: ChatWorkRequest)(priority: Priority = PriorityNormal): Unit = {
    logger.info(s"Enqueue request: $request with priority $priority")
    priority match {
      case PriorityRealTime => realTimeQueue.add(request)
      case PriorityNormal => normalQueue.add(request)
      case PriorityLow => lowQueue.add(request)
    }
  }

  protected def execute(): Unit = {
    dequeue() match {
      case None => // NOP
      case Some(req) =>
        req.execute(entityIOContext) map {
          res =>
            ApiHub.system.eventStream.publish(res)
        }
    }
  }

  protected def dequeue(): Option[ChatWorkRequest] = {
    logger.info(s"Queue size: Realtime: ${realTimeQueue.size()}, Normal: ${normalQueue.size()}, Low: ${lowQueue.size()}")
    realTimeQueue.peek() match {
      case r: ChatWorkRequest => Some(r)
      case null =>
        (normalQueue.size(), lowQueue.size()) match {
          case (0, 0) =>
            lowToNormalCount.set(0)
            None
          case (0, l) =>
            lowToNormalCount.set(0)
            Some(lowQueue.poll())
          case (n, 0) =>
            lowToNormalCount.set(0)
            Some(normalQueue.poll())
          case (n, l) =>
            if (lowToNormalCount.get > lowToNormalRatio) {
              lowToNormalCount.set(0)
              Some(lowQueue.poll())
            } else {
              lowToNormalCount.incrementAndGet()
              Some(normalQueue.poll())
            }
        }
    }
  }
}

object ApiHub {
  val system = ActorSystem("cw-apihub")

  def props(context: EntityIOContext[Future]): Props = Props(ApiHub(context))
}



