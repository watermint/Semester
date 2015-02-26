package semester.foundation.fextile.stage

import java.util.concurrent.atomic.AtomicReference

import semester.foundation.fextile.scene.Scene

import scalafx.application.JFXApp.{PrimaryStage => FXPrimaryStage}

class PrimaryStage {
  private val __primaryStage = new FXPrimaryStage()

  private val __scene = new AtomicReference[Scene]()

  private[fextile] def _primaryStage(): FXPrimaryStage = __primaryStage

  def scene: Scene = __scene.get()
  def scene_=(s: Scene): Unit = {
    __scene.set(s)
  }

  def title: String = __primaryStage.title.value
  def title_=(t: String): Unit = {
    __primaryStage.title = t
  }

  def width: Double = __primaryStage.width.value
  def width_=(w: Double): Unit = {
    __primaryStage.width = w
  }

  def height: Double = __primaryStage.height.value
  def height_=(h: Double): Unit = {
    __primaryStage.height = h
  }
}
