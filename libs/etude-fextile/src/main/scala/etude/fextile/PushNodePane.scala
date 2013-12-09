package etude.fextile

import scala.collection.mutable
import scalafx.scene.Node
import scalafx.scene.layout._
import scalafx.beans.property.DoubleProperty
import scalafx.animation.TranslateTransition

/**
 * UINavigationController like push/pop view.
 * @param firstNode first node.
 */
class PushNodePane(firstNode: Node) extends Pane {
  private val nodes: mutable.Stack[Node] = mutable.Stack[Node](firstNode)
  private val tape: GridPane = new GridPane {
    add(firstNode, 0, 0)
    columnConstraints = List(baseWidthConstraints())
    rowConstraints = List(baseHeightConstraints())
  }

  lazy val paneWidth: DoubleProperty = new DoubleProperty()
  lazy val paneHeight: DoubleProperty = new DoubleProperty()

  content = tape

  hgrow = Priority.ALWAYS
  vgrow = Priority.ALWAYS

  paneHeight.bind(this.height)
  paneWidth.bind(this.width)
  paneWidth.onChange {
    tape.translateX = -paneWidth.value * (nodes.size - 1)
  }

  protected def baseWidthConstraints(): ColumnConstraints = {
    new ColumnConstraints {
      hgrow = Priority.ALWAYS
      minWidth.bind(paneWidth)
      prefWidth.bind(paneWidth)
      maxWidth.bind(paneWidth)
    }
  }

  protected def baseHeightConstraints(): RowConstraints = {
    new RowConstraints {
      vgrow = Priority.ALWAYS
      minHeight.bind(paneHeight)
      prefHeight.bind(paneHeight)
      maxHeight.bind(paneHeight)
    }
  }

  protected def pushTransition(index: Int): Unit = {
    new TranslateTransition {
      node = tape
      toX = -paneWidth.value * index
      fromX = -paneWidth.value * (index - 1)
    }.play()
  }

  protected def popTransition(index: Int): Unit = {
    new TranslateTransition {
      node = tape
      toX = -paneWidth.value * (index - 1)
      fromX = -paneWidth.value * index
    }.play()
  }

  protected def emptyNode: Node = {
    new Pane()
  }

  case class NodeCannotReusableOnPushNodePaneException(message: String, cause: Throwable) extends Exception(message, cause)

  def pushNode(node: Node): Node = {
    tape.columnConstraints = (0 to nodes.size).map(i => baseWidthConstraints()).toList
    try {
      tape.add(node, nodes.size, 0)
    } catch {
      case e: IllegalArgumentException =>
        throw NodeCannotReusableOnPushNodePaneException(
          message = "Node cannot reusable on push node pane. Please recreate your node each time on pushView.",
          cause = e
        )
    }
    pushTransition(nodes.size)
    nodes.push(node)
    node
  }

  def popNode(): Node = {
    val node = nodes.pop()
    tape.add(emptyNode, nodes.size, 0)
    popTransition(nodes.size)
    node
  }
}