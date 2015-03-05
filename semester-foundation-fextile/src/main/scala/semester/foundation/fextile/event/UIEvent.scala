package semester.foundation.fextile.event

import javafx.{event => fxe}

trait UIEvent[FXE <: fxe.Event]
  extends Event {

  val fxEvent: FXE

  def consume(): Unit = fxEvent.consume()
}
