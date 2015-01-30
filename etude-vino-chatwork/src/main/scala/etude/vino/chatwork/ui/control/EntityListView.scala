package etude.vino.chatwork.ui.control

import etude.vino.chatwork.ui.UIStyles

import scalafx.scene.control.{ListCell, ListView}

trait EntityListView[E] extends ListView[E] {
  def listCellForEntity(): ListCell[E]

  margin = UIStyles.paddingInsets

  cellFactory = {
    _ =>
      listCellForEntity()
  }
}
