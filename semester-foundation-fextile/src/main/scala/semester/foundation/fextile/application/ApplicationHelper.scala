package semester.foundation.fextile.application

import javafx.application.{Application => JavaFXApplication}
import javafx.stage.Stage

private[fextile] class ApplicationHelper extends JavaFXApplication {
  def start(stage: Stage): Unit = {
    FextileApp.stage = Some(stage)
    FextileApp.application.foreach {
      app =>
        stage.show()
    }
  }
}
