package semester.foundation.fextile.event

import semester.foundation.fextile.application.FextileApp

trait ApplicationEvent
  extends Event

case class ApplicationWillLaunch(source: FextileApp,
                                 args: Array[String])
  extends ApplicationEvent
