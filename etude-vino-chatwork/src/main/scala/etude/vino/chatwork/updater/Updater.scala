package etude.vino.chatwork.updater

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef, Props}
import etude.pintxos.chatwork.domain.service.v0.request.GetUpdateRequest
import etude.pintxos.chatwork.domain.service.v0.response.{GetUpdateResponse, InitLoadResponse}
import etude.vino.chatwork.api.{ApiEnqueue, ApiHub, PriorityHigh}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

case class Updater(apiHub: ActorRef, updateClockCycleInSeconds: Long)
  extends Actor {

  implicit val executionContext: ExecutionContext = ApiHub.system.dispatcher

  case class ScheduledUpdate()

  def receive: Receive = {
    case u: ScheduledUpdate =>
      apiHub ! ApiEnqueue(GetUpdateRequest(), PriorityHigh)

    case r: InitLoadResponse =>
      ApiHub.system.scheduler.scheduleOnce(Duration.create(updateClockCycleInSeconds, TimeUnit.SECONDS), self, ScheduledUpdate())

    case r: GetUpdateResponse =>
      ApiHub.system.scheduler.scheduleOnce(Duration.create(updateClockCycleInSeconds, TimeUnit.SECONDS), self, ScheduledUpdate())
  }
}

object Updater {
  def props(apiHub: ActorRef, updateClockCycleInSeconds: Long): Props = Props(Updater(apiHub, updateClockCycleInSeconds))
}