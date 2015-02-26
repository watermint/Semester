package semester.foundation.fextile.scene

import java.util.concurrent.atomic.AtomicReference

import scalafx.scene.{Parent => FXParent}

class Parent {
  private val fxParent = new AtomicReference[FXParent]()
}
