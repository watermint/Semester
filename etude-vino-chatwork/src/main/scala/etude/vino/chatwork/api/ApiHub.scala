package etude.vino.chatwork.api

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{ConcurrentLinkedQueue, ScheduledThreadPoolExecutor, TimeUnit}

import etude.epice.logging.LoggerFactory
import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.V0AsyncEntityIO
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.ChatWorkRequest
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.ChatWorkResponse

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

case class ApiHub(context: EntityIOContext[Future])
  extends V0AsyncEntityIO {

  private val logger = LoggerFactory.getLogger(getClass)

  private val clockCycleInMillis = 5000

  private val scheduledExecutor: ScheduledThreadPoolExecutor = {
    val executor = new ScheduledThreadPoolExecutor(1)

    executor.scheduleAtFixedRate(new Runnable {
      def run(): Unit = {
        execute()
      }
    }, clockCycleInMillis, clockCycleInMillis, TimeUnit.MILLISECONDS)

    executor
  }

  private val subscribers = ArrayBuffer[ApiSubscriber]()

  private val lowToNormalRatio = 5

  private val lowToNormalCount = new AtomicInteger()

  private val realTimeQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val normalQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val lowQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()


  def shutdown(): Unit = {
    scheduledExecutor.shutdown()
  }

  def addSubscriber(subscriber: ApiSubscriber): Unit = {
    subscribers += subscriber
  }

  def removeSubscriber(subscriber: ApiSubscriber): Unit = {
    subscribers -= subscriber
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
        implicit val executor = getExecutionContext(context)
        req.execute(context) map {
          res =>
            subscribers.foreach {
              receiver =>
                if (receiver.receive.isDefinedAt(res)) {
                  receiver.receive(res)
                }
            }
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
