package etude.app.arrabbiata.ui

import javafx.event.{Event, EventHandler}

trait UI {
  def event[T <: Event](f: T => Unit): EventHandler[T] = new EventHandler[T] {
    def handle(e: T) = f(e)
  }
}
