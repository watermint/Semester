package etude.vino.chatwork.ui.control

import java.time._
import java.time.chrono.Chronology
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.concurrent.atomic.AtomicReference

import etude.vino.chatwork.ui.{UI, UIMessage, UIStyles}
import org.controlsfx.control.PopOver

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control._
import scalafx.scene.layout.GridPane
import scalafx.util.StringConverter

class PeriodStartView extends Button {

  val defaultLabel = "Start Date/Time"

  val startGroup = new ToggleGroup()

  trait TimeType {
    val name: String
    val colSpan: Int = 1

    def labelForStartButton: String

    def value: TimeValue
  }

  trait TimeValue {
    def asInstant: Option[Instant]
  }

  case class TimeValueNoConstraints() extends TimeValue {
    def asInstant: Option[Instant] = None
  }

  case class TimeValueDate(localDate: LocalDate) extends TimeValue {
    def zonedDateTime: ZonedDateTime = localDate.atStartOfDay().atZone(ZoneId.systemDefault())

    def asInstant: Option[Instant] = Some(zonedDateTime.toInstant)

    override def toString: String = localDate.toString
  }

  case class TimeValueMinutes(minutes: Int) extends TimeValue {
    def asInstant: Option[Instant] = Some(Instant.now().minus(minutes, ChronoUnit.MINUTES))

    override def toString: String = minutes.toString
  }

  case class TimeValueHours(hours: Int) extends TimeValue {
    def asInstant: Option[Instant] = Some(Instant.now().minus(hours, ChronoUnit.HOURS))

    override def toString: String = hours.toString
  }

  case class TimeValueDays(days: Int) extends TimeValue {
    def asInstant: Option[Instant] = Some(Instant.now().minus(days, ChronoUnit.DAYS))

    override def toString: String = days.toString
  }

  case object TimeTypeNoConstraints extends TimeType {
    val name: String = "No constraints"
    override val colSpan: Int = 2

    def labelForStartButton: String = defaultLabel

    def value: TimeValue = TimeValueNoConstraints()
  }

  case object TimeTypeDate extends TimeType {
    val name: String = "Date"

    def labelForStartButton: String = s"From $value"

    def value: TimeValue = TimeValueDate(
      valueControlDate.getValue match {
        case null => LocalDate.now()
        case d => d
      }
    )
  }

  case object TimeTypeMinutes extends TimeType {
    val name: String = "Minutes"

    def labelForStartButton: String = s"$value minutes"

    def value: TimeValue = TimeValueMinutes(valueControlMinutes.value.get)
  }

  case object TimeTypeHours extends TimeType {
    val name: String = "Hours"

    def labelForStartButton: String = s"$value hours"

    def value: TimeValue = TimeValueMinutes(valueControlHours.value.get)
  }

  case object TimeTypeDays extends TimeType {
    val name: String = "Days"

    def labelForStartButton: String = s"$value days"

    def value: TimeValue = TimeValueMinutes(valueControlDays.value.get)
  }

  val timeTypes = Seq(
    TimeTypeNoConstraints,
    TimeTypeDate,
    TimeTypeMinutes,
    TimeTypeHours,
    TimeTypeDays
  )

  val radioButtons: Map[TimeType, RadioButton] = {
    timeTypes.map {
      tt =>
        tt -> new RadioButton {
          text = tt.name
          toggleGroup = startGroup
          onAction = handle {
            selectTimeType(tt)
          }
        }
    }.toMap
  }

  val numberConverter = new StringConverter[Int] {
    def fromString(string: String): Int = Integer.parseInt(string)

    def toString(t: Int): String = t.toString
  }

  val valueControlDate = new DatePicker {
    chronology = Chronology.ofLocale(Locale.JAPANESE)
    onAction = handle {
      selectTimeType(TimeTypeDate)
    }
  }

  val valueControlMinutes = new ComboBox[Int] {
    editable = true
    onAction = handle {
      selectTimeType(TimeTypeMinutes)
    }
    converter = numberConverter
    items = ObservableBuffer(
      15,
      30
    )
  }

  val valueControlHours = new ComboBox[Int] {
    editable = true
    onAction = handle {
      selectTimeType(TimeTypeHours)
    }
    converter = numberConverter
    items = ObservableBuffer(
      1,
      2,
      4,
      8
    )
  }

  val valueControlDays = new ComboBox[Int] {
    editable = true
    onAction = handle {
      selectTimeType(TimeTypeDays)
    }
    converter = numberConverter
    items = ObservableBuffer(
      1,
      7,
      14,
      30
    )
  }

  val valueControls = Map(
    TimeTypeDate -> valueControlDate,
    TimeTypeMinutes -> valueControlMinutes,
    TimeTypeHours -> valueControlHours,
    TimeTypeDays -> valueControlDays
  )

  def selectTimeType(timeType: TimeType): Unit = {
    UI.ref ! new UIMessage {
      def perform(): Unit = {
        radioButtons.get(timeType).foreach(_.selected = true)
        thisButton.text = timeType.labelForStartButton
        selectedTimeType.set(timeType)
        selectedTimeValue.set(timeType.value)
        popOver.hide()
        onSelect(timeType.value)
      }
    }
  }

  val popOver = new PopOver {
    setContentNode(
      new GridPane {
        padding = Insets(UIStyles.paddingPixel)
        hgap = UIStyles.spacingWidth
        vgap = UIStyles.spacingWidth

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

        timeTypes.zipWithIndex.foreach {
          case (tt, row) =>
            radioButtons.get(tt).foreach {
              button =>
                add(button, 0, row + 1, tt.colSpan, 1)
            }
            valueControls.get(tt).foreach {
              control =>
                add(control, 1, row + 1)
            }
        }
      }.delegate
    )
  }

  val selectedTimeType = new AtomicReference[TimeType]()

  val selectedTimeValue = new AtomicReference[TimeValue]()

  def selectedTime: Option[Instant] = selectedTimeValue.get() match {
    case null => None
    case v: TimeValue => v.asInstant
  }

  def onSelect(time: TimeValue) = {}

  val thisButton: Button = this

  text = defaultLabel

  onAction = handle {
    popOver.show(this)
  }

}
