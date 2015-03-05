package semester.foundation.fextile.event

import akka.actor.{ActorRef, Props}
import semester.foundation.fextile.application.Fextile

trait EventSource {
  def props: Option[Props] = None

  private[fextile] val eventActor: Option[ActorRef] = props.map(Fextile.system.actorOf)

  private[fextile] var eventSupervisor: Option[EventSource] = None

  def currentActor: ActorRef = {
    eventActor match {
      case Some(a) => a
      case None =>
        eventSupervisor match {
          case Some(s) => s.currentActor
          case None => Fextile.appDefault
        }
    }
  }
}
