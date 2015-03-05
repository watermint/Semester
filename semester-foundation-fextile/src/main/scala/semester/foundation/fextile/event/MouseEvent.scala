package semester.foundation.fextile.event

import javafx.scene.{input => fxsi}

trait MouseEvent
  extends UIEvent[fxsi.MouseEvent]

case class MouseDragDetected(fxEvent: fxsi.MouseEvent,
                             source: EventSource)
  extends MouseEvent

case class MouseClicked(fxEvent: fxsi.MouseEvent,
                        source: EventSource)
  extends MouseEvent

case class MouseDragged(fxEvent: fxsi.MouseEvent,
                        source: EventSource)
  extends MouseEvent

case class MouseEntered(fxEvent: fxsi.MouseEvent,
                        source: EventSource)
  extends MouseEvent

case class MouseEnteredTarget(fxEvent: fxsi.MouseEvent,
                              source: EventSource)
  extends MouseEvent

case class MouseExited(fxEvent: fxsi.MouseEvent,
                       source: EventSource)
  extends MouseEvent

case class MouseExitedTarget(fxEvent: fxsi.MouseEvent,
                             source: EventSource)
  extends MouseEvent

case class MouseMoved(fxEvent: fxsi.MouseEvent,
                      source: EventSource)
  extends MouseEvent

case class MousePressed(fxEvent: fxsi.MouseEvent,
                        source: EventSource)
  extends MouseEvent

case class MouseReleased(fxEvent: fxsi.MouseEvent,
                         source: EventSource)
  extends MouseEvent
