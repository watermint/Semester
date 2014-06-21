package etude.app.arrabbiata.ui.control

import java.util.concurrent.atomic.AtomicReference
import javafx.beans.value.{ChangeListener, ObservableValue}

import scalafx.scene.control.ListView

class DomainListView[T] extends ListView[T] {
  val selected = new AtomicReference[T]()

  selectionModel().selectedItemProperty().addListener(
    new ChangeListener[T] {
      override def changed(observable: ObservableValue[_ <: T], oldValue: T, newValue: T): Unit = {
        selected.set(newValue)
        onSelected(newValue)
      }
    }
  )

  def onSelected(t: T): Unit = {}
}
