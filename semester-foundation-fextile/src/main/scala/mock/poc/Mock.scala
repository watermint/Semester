package mock.poc

import akka.actor.ActorRef
import semester.foundation.fextile.application.FextileApp
import semester.foundation.fextile.stage.PrimaryStage

object Mock extends FextileApp {

  primaryStage = new PrimaryStage {
    title = "Mock"
    width = 800
    height = 600
  }

  val actor: ActorRef = _
}
