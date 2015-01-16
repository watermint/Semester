package etude.vino.chatwork.ui.control

import java.time.chrono.Chronology
import java.util.Locale

import etude.vino.chatwork.ui.UIStyles
import org.controlsfx.control.PopOver

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control._
import scalafx.scene.layout.GridPane

class PeriodStartView extends Button {
  text = "Start Date/Time"

  val popOver = new PopOver()

  val startGroup = new ToggleGroup()

  onAction = handle {
    popOver.show(this)
  }

  popOver.setContentNode(
    new GridPane {
      padding = Insets(UIStyles.padding)
      hgap = UIStyles.spacing
      vgap = UIStyles.spacing

      add(
        child = new Label {
          text = "Date/Time from"
          style = UIStyles.h4Style
        },
        columnIndex = 0,
        rowIndex = 0,
        colspan = 2,
        rowspan = 1
      )
      add(
        child = new RadioButton {
          text = "No constraints"
          toggleGroup = startGroup
        },
        columnIndex = 0,
        rowIndex = 1,
        colspan = 2,
        rowspan = 1
      )
      add(
        child = new RadioButton {
          text = "Date"
          toggleGroup = startGroup
        },
        columnIndex = 0,
        rowIndex = 2
      )
      add(
        child = new DatePicker {
          chronology = Chronology.ofLocale(Locale.JAPANESE)
        },
        columnIndex = 1,
        rowIndex = 2
      )
      add(
        child = new RadioButton {
          text = "Minutes"
          toggleGroup = startGroup
        },
        columnIndex = 0,
        rowIndex = 3
      )
      add(
        child = new ComboBox[Int] {
          editable = true
          items = ObservableBuffer(
            15,
            30
          )
        },
        columnIndex = 1,
        rowIndex = 3
      )
      add(
        child = new RadioButton {
          text = "Hour"
          toggleGroup = startGroup
        },
        columnIndex = 0,
        rowIndex = 4
      )
      add(
        child = new ComboBox[Int] {
          editable = true
          items = ObservableBuffer(
            1,
            2,
            4,
            8,
            24
          )
        },
        columnIndex = 1,
        rowIndex = 4
      )
    }.delegate
  )
}
