package etude

import javafx.event.{EventHandler, Event}

package object fextile {
  def event[T <: Event](f: T => Unit): EventHandler[T] = new EventHandler[T] {
    def handle(e: T) = f(e)
  }
}
