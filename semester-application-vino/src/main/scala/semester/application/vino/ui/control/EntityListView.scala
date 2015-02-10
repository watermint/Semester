package semester.application.vino.ui.control

import semester.application.vino.ui.UIStyles

import scalafx.scene.control.{ListCell, ListView}

trait EntityListView[E] extends ListView[E] {
  def listCellForEntity(): ListCell[E]

  margin = UIStyles.paddingInsets

  cellFactory = {
    _ =>
      listCellForEntity()
  }
}
