package etude.epice.foundation.atomic

import java.util.concurrent.atomic.AtomicReference

case class Reference[T]() {
  private val reference = new AtomicReference[T]()

  def set(value: T): Unit = reference.set(value)

  def get(): Option[T] = reference.get match {
    case null => None
    case value => Some(value)
  }
}
