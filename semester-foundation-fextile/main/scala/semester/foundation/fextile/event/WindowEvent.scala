package semester.foundation.fextile.event

import javafx.{stage => fxs}

trait WindowEvent
  extends UIEvent[fxs.WindowEvent]

case class WindowCloseRequest(fxEvent: fxs.WindowEvent,
                              source: EventSource)
  extends WindowEvent

case class WindowHidden(fxEvent: fxs.WindowEvent,
                        source: EventSource)
  extends WindowEvent

case class WindowHiding(fxEvent: fxs.WindowEvent,
                        source: EventSource)
  extends WindowEvent

case class WindowShown(fxEvent: fxs.WindowEvent,
                       source: EventSource)
  extends WindowEvent

case class WindowShowing(fxEvent: fxs.WindowEvent,
                         source: EventSource)
  extends WindowEvent
