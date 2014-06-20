package etude.desktop.fextile

import scalafx.scene.layout.{HBox, Priority, VBox}

trait Container

class VContainer extends VBox with Container {
  hgrow = Priority.ALWAYS
  vgrow = Priority.ALWAYS
  spacing = 8
  styleClass = Seq("container")
}

class HContainer extends HBox with Container {
  hgrow = Priority.ALWAYS
  vgrow = Priority.ALWAYS
  spacing = 8
  styleClass = Seq("container")
}
