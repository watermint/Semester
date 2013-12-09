package etude.fextile

import javafx.scene.layout.{GridPane => FxGridPane}
import scalafx.scene.layout._
import scalafx.scene.Node
import scalafx.beans.property.DoubleProperty
import scalafx.geometry.Pos
import scala.collection.mutable
import scala.Some

/**
 *
 */
class GridRow extends VBox {
  private val nodes: mutable.ListBuffer[Node] = mutable.ListBuffer[Node]()
  private val spans: mutable.HashMap[Node, Seq[GridSpan]] = mutable.HashMap[Node, Seq[GridSpan]]()
  private var currentCol: Int = 0
  private var currentRow: Int = 0

  lazy val paneWidth: DoubleProperty = new DoubleProperty()
  lazy val container = new GridPane() {
    style = "-fx-background-color: #404040;"
    alignment = Pos.CENTER
    columnConstraints = (1 to 12).map(i => new ColumnConstraints {
      percentWidth = 100.0 / 12.0
    })
    content = (1 to 12).map(i => new Pane())
  }

  style = "-fx-background-color: #804040;"
  content = container
  alignment = Pos.TOP_CENTER
  hgrow = Priority.ALWAYS
  vgrow = Priority.ALWAYS

  trait SizeConstraints extends DeviceSize {
    val containerWidth: Option[Int]
    val columnWidth: Option[Int]
  }

  case class SizeConstraintsExtraSmall() extends SizeConstraints with DeviceSizeExtraSmall {
    val containerWidth: Option[Int] = None
    val columnWidth: Option[Int] = None
  }

  case class SizeConstraintsSmall() extends SizeConstraints with DeviceSizeSmall {
    val containerWidth: Option[Int] = Some(750)
    val columnWidth: Option[Int] = Some(60)
  }

  case class SizeConstraintsMedium() extends SizeConstraints with DeviceSizeMedium {
    val containerWidth: Option[Int] = Some(970)
    val columnWidth: Option[Int] = Some(78)
  }

  case class SizeConstraintsLarge() extends SizeConstraints with DeviceSizeLarge {
    val containerWidth: Option[Int] = Some(1170)
    val columnWidth: Option[Int] = Some(95)
  }

  lazy val sizeConstraints: Seq[SizeConstraints] = Seq(
    SizeConstraintsExtraSmall(),
    SizeConstraintsSmall(),
    SizeConstraintsMedium(),
    SizeConstraintsLarge()
  )

  protected def resizeContainer(): Unit = {
    sizeConstraints.find(_.widthRange.contains(paneWidth.value.toInt)) match {
      case Some(sc) =>
        sc.containerWidth match {
          case Some(w: Int) =>
            container.minWidth = w
            container.maxWidth = w
          case _ =>
        }
        sc.columnWidth match {
          case Some(w: Int) =>
            container.columnConstraints = (1 to 12).map(i => new ColumnConstraints {
              percentWidth = 100.0 / 12.0
              maxWidth = w
            })
          case _ =>
            container.columnConstraints = (1 to 12).map(i => new ColumnConstraints {
              percentWidth = 100.0 / 12.0
            })
        }
    }
  }

  protected def offsetAndSpanForNode(node: Node): Option[Pair[Option[Int], Int]] = {
    spans.get(node) match {
      case None => None
      case Some(s) => s.find(_.widthRange.contains(paneWidth.value.toInt)) match {
        case Some(sc) => Some(sc.offset -> sc.span)
        case _ => None
      }
    }
  }

  protected def placeNode(node: Node, col: Int, row: Int, span: Int): Unit = {
    if (container.children.contains(node)) {
      FxGridPane.setColumnIndex(node, col)
      FxGridPane.setRowIndex(node, row)
    } else {
      container.add(node, col, row)
    }
    FxGridPane.setColumnSpan(node, span)
  }

  protected def offsetAppend(node: Node, offset: Int, span: Int = 1): Unit = {
    if (currentCol + offset + span >= 12) {
      currentRow += 1
      currentCol = offset
    } else if (offset > currentCol) {
      currentCol = offset
    }
    placeNode(node, currentCol, currentRow, span)
    currentCol += span
  }

  protected def simpleAppend(node: Node, span: Int = 1): Unit = {
    placeNode(node, currentCol, currentRow, span)
    if (currentCol + span >= 12) {
      currentRow += 1
      currentCol = 0
    } else {
      currentCol += span
    }
  }

  protected def rebalance(): Unit = {
    currentCol = 0
    currentRow = 0

    nodes.foreach {
      node =>
        offsetAndSpanForNode(node) match {
          case None => simpleAppend(node)
          case Some(os) =>
            val spanOfNode = os._2
            os._1 match {
              case None => simpleAppend(node, spanOfNode)
              case Some(offset) => offsetAppend(node, offset, spanOfNode)
            }
        }
    }
  }

  resizeContainer()
  paneWidth.bind(this.width)
  paneWidth.onChange {
    resizeContainer()
    rebalance()
  }

  def add(node: Node, span: GridSpan*): Unit = {
    nodes += node
    spans.put(node, span)

    offsetAndSpanForNode(node) match {
      case None => simpleAppend(node)
      case Some(os) =>
        val spanOfNode = os._2
        os._1 match {
          case None => simpleAppend(node, spanOfNode)
          case Some(offset) => offsetAppend(node, offset, spanOfNode)
        }
    }
    rebalance()
  }
}

