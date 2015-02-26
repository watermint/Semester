package semester.foundation.fextile.application

import akka.actor.Actor
import semester.foundation.fextile.event.WindowHidden

class ApplicationDefault extends Actor {
  override def receive: Receive = {
    case (_, _: WindowHidden) =>
      Fextile.shutdown()
  }
}
