package semester.foundation.fextile.application

import javafx.{application => fxa}

import akka.actor._
import semester.foundation.fextile.event._

class Fextile extends Actor with Stash {
  private var appActor: Option[ActorRef] = None

  def receive: Receive = {
    case launcher: ApplicationLauncher =>
      launcher.app.props foreach {
        props =>
          val actor = Fextile.system.actorOf(props)
          appActor = Some(actor)
          actor ! ApplicationWillLaunch(launcher.app, launcher.args)
      }
      launcher.launch()
      unstashAll()

    case (source: EventSource, e: UIEvent[_]) =>
      source.currentActor ! e

    case (source: EventSource, e: Event) =>
      appActor match {
        case Some(app) => app ! e
        case None => stash()
      }
  }
}

object Fextile {
  def shutdown() = {
    fxa.Platform.exit()
    system.shutdown()
  }

  val system = ActorSystem("fextile")

  val router = system.actorOf(Props[Fextile])

  val appDefault = system.actorOf(Props[ApplicationDefault])
}
