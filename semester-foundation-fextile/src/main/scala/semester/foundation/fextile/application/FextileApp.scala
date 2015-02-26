package semester.foundation.fextile.application

import java.util.concurrent.atomic.AtomicReference
import javafx.application.{Application => JavaFXApplication}
import javafx.stage.{Stage => JavaFXStage}

import akka.actor.ActorRef
import semester.foundation.fextile.Fextile
import semester.foundation.fextile.stage.PrimaryStage

trait FextileApp {
  Fextile.ref ! this

  private val __primaryStage = new AtomicReference[PrimaryStage]()

  val actor: ActorRef

  def primaryStage: PrimaryStage = __primaryStage.get()
  def primaryStage_=(p: PrimaryStage): Unit = {
    __primaryStage.set(p)
  }

  def main(args: Array[String]): Unit = {
    FextileApp.args = Some(args)
    FextileApp.application = Some(this)
    JavaFXApplication.launch(classOf[ApplicationHelper], args: _*)
  }
}

object FextileApp {
  private[fextile] var application: Option[FextileApp] = None
  private[fextile] var stage: Option[JavaFXStage] = None
  private[fextile] var args: Option[Array[String]] = None
}