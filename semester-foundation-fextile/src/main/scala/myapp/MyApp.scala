package myapp

import javafx.scene.paint.Color

import akka.actor.{Actor, Props}
import semester.foundation.fextile.application.{Fextile, FextileApp}
import semester.foundation.fextile.event.{WindowHidden, MouseClicked}
import semester.foundation.fextile.scene.Scene
import semester.foundation.fextile.stage.PrimaryStage

class MyApp extends Actor {
  override def receive: Receive = {
    case (s: Scene, e: MouseClicked) =>
      s.fill = Color.RED

    case (_, _: WindowHidden) =>
      Fextile.shutdown()
  }
}

object MyApp extends FextileApp {
  // Do layout just like JavaFX/ScalaFX application
  stage = new PrimaryStage {
    title = "My Application"
    width = 800
    height = 600
    scene = new Scene {
      fill = Color.BLUE
    }
  }

  override def props: Option[Props] = Some(Props[MyApp])
}
