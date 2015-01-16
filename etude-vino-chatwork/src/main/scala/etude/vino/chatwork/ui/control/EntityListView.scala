package etude.vino.chatwork.ui.control

import etude.vino.chatwork.ui.UIStyles

import scalafx.geometry.Insets
import scalafx.scene.control.{ListCell, ListView}

trait EntityListView[E] extends ListView[E] {
  def listCellForEntity(): ListCell[E]

  margin = Insets(UIStyles.spacing)

  cellFactory = {
    _ =>
      listCellForEntity()
  }
}
