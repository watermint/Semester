package etude.vino.chatwork.api

import java.time.Instant
import java.util.concurrent.{ConcurrentLinkedQueue, TimeUnit}

import akka.actor.{Actor, ActorSystem, Props}
import etude.epice.logging.LoggerFactory
import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.V0AsyncEntityIO
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.ChatWorkRequest

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

case class ApiHub(entityIOContext: EntityIOContext[Future])
  extends V0AsyncEntityIO with Actor {

  private val logger = LoggerFactory.getLogger(getClass)

  private val clockCycleInMillis = 3000

  private val realTimeQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val normalQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val lowQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  private val lowerQueue = new ConcurrentLinkedQueue[ChatWorkRequest]()

  case class ApiTick()

  implicit val executionContext: ExecutionContext = getExecutionContext(entityIOContext)

  private val errorQueue = ArrayBuffer[Exception]()

  private val errorThreshold = 3

  private var errorWaitLockTime = Instant.EPOCH

  private val waitTimeOnErrorInSeconds = 60

  private val executionResultTimeout = Duration(10, SECONDS)

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
      case PriorityRealTime =>
        realTimeQueue.add(request)
        self ! ApiTick()
      case PriorityNormal => normalQueue.add(request)
      case PriorityLow => lowQueue.add(request)
      case PriorityLower => lowerQueue.add(request)
    }
  }

  protected def execute(): Unit = {
    if (errorWaitLockTime.isBefore(Instant.now())) {
      dequeue() match {
        case None => // NOP
        case Some(req) =>
          logger.info(s"Execute: $req")
          try {
            val res = Await.result(req.execute(entityIOContext), executionResultTimeout)
            errorQueue.clear()
            ApiHub.system.eventStream.publish(res)
          } catch {
            case e: Exception =>
              logger.error(s"Error on $req", e)
          }
      }
    }
  }

  protected def onExecutionErrors(request: ChatWorkRequest, exception: Exception): Unit = {
    errorQueue += exception
    logger.error(s"Failed to execute request: $request", exception)
    if (errorQueue.size > errorThreshold) {
      resetContextSession(entityIOContext)
      errorWaitLockTime = Instant.now().plusSeconds(waitTimeOnErrorInSeconds)
      logger.error(s"Execution shutdown due to continuous errors until $errorWaitLockTime, All contexts are cleared")
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

  def props(context: EntityIOContext[Future]): Props = Props(ApiHub(context))
}



