package semester.application.vino.service.updater

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef, Props}
import semester.foundation.utilities.qos.TimeoutSemaphore
import semester.service.chatwork.domain.service.v0.request.GetUpdateRequest
import semester.service.chatwork.domain.service.v0.response.{GetUpdateResponse, InitLoadResponse}
import semester.application.vino.domain.lifecycle.SystemRepository
import semester.application.vino.service.api._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

case class Updater(apiHub: ActorRef, updateClockCycleInSeconds: Long)
  extends Actor {

  implicit val executionContext: ExecutionContext = Api.system.dispatcher

  private val semaphore = new TimeoutSemaphore(java.time.Duration.ofSeconds(60))

  case class ScheduledUpdate()

  case class GetUpdateOfOffline(lastId: String)

  SystemRepository.lastId().foreach {
    lastId =>
      self ! GetUpdateOfOffline(lastId)
  }

  def receive: Receive = {
    case u: ScheduledUpdate =>
      if (semaphore.tryAcquire()) {
        apiHub ! ApiEnqueue(GetUpdateRequest(), PriorityP1)
      }

    case g: GetUpdateOfOffline =>
      apiHub ! ApiEnqueue(GetUpdateRequest(lastId = Some(g.lastId)), PriorityP1)

    case r: GetUpdateResponse =>
      semaphore.release()
      r.lastId.foreach(SystemRepository.updateLastId)

    case r: InitLoadResponse =>
      Api.system.scheduler.schedule(
        Duration.create(updateClockCycleInSeconds, TimeUnit.SECONDS),
        Duration.create(updateClockCycleInSeconds, TimeUnit.SECONDS),
        self,
        ScheduledUpdate()
      )
  }
}

object Updater {
  def props(apiHub: ActorRef, updateClockCycleInSeconds: Long): Props = Props(Updater(apiHub, updateClockCycleInSeconds))
}