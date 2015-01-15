package etude.vino.chatwork.ui.control

import etude.manieres.domain.model.Entity
import etude.vino.chatwork.ui.UIStyles

import scalafx.geometry.Insets
import scalafx.scene.control.{ListCell, ListView}

trait DomainListView[E <: Entity[_]] extends ListView[E] {
  def listCellForDomain(): ListCell[E]

  margin = Insets(UIStyles.spacing)

  cellFactory = {
    _ =>
      listCellForDomain()
  }
}
