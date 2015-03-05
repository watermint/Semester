package semester.foundation.fextile.application

import javafx.{application => fxa}

private[fextile]
case class ApplicationLauncher(app: FextileApp,
                               args: Array[String]) {

  def launch(): Unit = {
    ApplicationHelper.launcher = Some(this)
    fxa.Application.launch(classOf[ApplicationHelper], args: _*)
  }

  def enqueue(): Unit = {
    Fextile.router ! this
  }
}
