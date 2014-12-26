package etude.vino.chatwork.api

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{ConcurrentLinkedQueue, ScheduledThreadPoolExecutor, TimeUnit}

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.V0AsyncEntityIO
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.ChatWorkRequest
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.ChatWorkResponse

import scala.concurrent.Future

case class ApiHub(context: EntityIOContext[Future])
  extends V0AsyncEntityIO {

  private val clockCycleInMillis = 500

  private val scheduledExecutor: ScheduledThreadPoolExecutor = {
    val executor = new ScheduledThreadPoolExecutor(1)

    executor.scheduleAtFixedRate(new Runnable {
      def run(): Unit = {
        execute()
      }
    }, clockCycleInMillis, clockCycleInMillis, TimeUnit.MILLISECONDS)

    executor
  }

  private val lowToNormalRatio = 5

  private val lowToNormalCount = new AtomicInteger()

  private val realTimeQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val normalQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val lowQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  trait Priority

  case object PriorityRealTime extends Priority

  case object PriorityNormal extends Priority

  case object PriorityLow extends Priority

  def enqueue(request: ChatWorkRequest)(priority: Priority = PriorityNormal): Unit = {
    priority match {
      case PriorityRealTime => realTimeQueue.offer(request)
      case PriorityNormal => normalQueue.offer(request)
      case PriorityLow => lowQueue.offer(request)
    }
  }

  protected def execute(): Unit = {
    dequeue() match {
      case None => // NOP
      case Some(r) =>

    }
  }

  protected def dispatch(request: ChatWorkRequest): Future[ChatWorkResponse] = {
    request.execute(context)
  }

  protected def dequeue(): Option[ChatWorkRequest] = {
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
