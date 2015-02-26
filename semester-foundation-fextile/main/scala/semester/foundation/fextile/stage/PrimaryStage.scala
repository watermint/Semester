package semester.foundation.fextile.stage

import javafx.{stage => fxs}

import semester.foundation.fextile.application.ApplicationHelper

class PrimaryStage
  extends Stage {

  override def createDelegate: fxs.Stage = {
    eventSupervisor = Some(ApplicationHelper.launcher.get.app)
    ApplicationHelper.stage.get
  }
}
