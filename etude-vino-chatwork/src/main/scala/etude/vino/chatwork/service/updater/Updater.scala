package etude.vino.chatwork.service.updater

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef, Props}
import etude.epice.utility.qos.TimeoutSemaphore
import etude.pintxos.chatwork.domain.service.v0.request.GetUpdateRequest
import etude.pintxos.chatwork.domain.service.v0.response.{GetUpdateResponse, InitLoadResponse}
import etude.vino.chatwork.service.api._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

case class Updater(apiHub: ActorRef, updateClockCycleInSeconds: Long)
  extends Actor {

  implicit val executionContext: ExecutionContext = Api.system.dispatcher

  private val semaphore = new TimeoutSemaphore(java.time.Duration.ofSeconds(60))

  case class ScheduledUpdate()

  def receive: Receive = {
    case u: ScheduledUpdate =>
      if (semaphore.tryAcquire()) {
        apiHub ! ApiEnqueue(GetUpdateRequest(), PriorityP1)
      }

    case _: GetUpdateResponse =>
      semaphore.release()

    case _: InitLoadResponse =>
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