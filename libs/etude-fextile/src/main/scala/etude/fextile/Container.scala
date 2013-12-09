package etude.fextile

import scalafx.scene.layout.{HBox, VBox}

trait Container

class VContainer extends VBox with Container {
  spacing = 8
  styleClass = Seq("container")
}

class HContainer extends HBox with Container {
  spacing = 8
  styleClass = Seq("container")
}
