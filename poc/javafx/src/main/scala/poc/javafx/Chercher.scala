package poc.javafx

import scalafx.application.JFXApp
import scalafx.scene._
import scalafx.scene.paint._
import scalafx.scene.layout._
import scalafx.scene.control._
import scalafx.geometry.Insets
import scalafx.scene.control.ScrollPane.ScrollBarPolicy
import javafx.event.{ActionEvent, EventHandler}

object Repository {
  def rooms: List[String] = List(
    "Areca",
    "DayStats",
    "PunchedTape",
    "Stenographer",
    "Aggregation",
    "Bootstrap",
    "Calendar",
    "ChatWork",
    "File",
    "Html",
    "Http",
    "Io",
    "Money",
    "QoS",
    "Region",
    "Religion"
  )

  def smartRooms: List[String] = List(
    "Unread",
    "Star",
    "Recent 24 Hours",
    "Recent 3 Days",
    "Recent 7 Days",
    "Recent 30 Days"
  )

  def timeline(room: String): List[String] = {
    (1 to (20 * room.length)).map(i => room + i).toList
  }
}

object Chercher extends JFXApp {
  case class ActionEventHandler(f: ActionEvent => Unit) extends EventHandler[ActionEvent] {
    def handle(e: ActionEvent) = f(e)
  }

  def actionEventHandler(f: ActionEvent => Unit): EventHandler[ActionEvent] = ActionEventHandler(f)

  def timelineRow(line: String): Label = new Label {
    text = line
  }

  def link(room: String): Hyperlink = new Hyperlink {
    text = room
    onAction = actionEventHandler {
      e =>
        timelineContainer.content = Repository.timeline(room).map(timelineRow)
    }
  }

  lazy val left = new ScrollPane {
    hbarPolicy = ScrollBarPolicy.NEVER
    content = new VBox {
      hgrow = Priority.ALWAYS
      prefWidth = 240
      padding = Insets(8)
      spacing = 8
      content = Seq(
        new Label("Rooms"),
        new VBox {
          padding = Insets(0, 0, 0, 8)
          content = Repository.rooms.map(link)
        },
        new Label("Smart Rooms"),
        new VBox {
          padding = Insets(0, 0, 0, 8)
          content = Repository.smartRooms.map(link)
        }
      )
    }
  }

  lazy val timelineContainer = new VBox {
    padding = Insets(8)
    spacing = 8
  }

  lazy val right = new ScrollPane {
    prefWidth = 560
    content = timelineContainer
  }

  stage = new JFXApp.PrimaryStage {
    title = "Chercher"
    width = 800
    height = 600
    scene = new Scene {
      fill = Color.BLUE
      root = new SplitPane {
        dividerPositions = List(0.2, 0.8): _*
        hgrow = Priority.ALWAYS
        vgrow = Priority.ALWAYS
        items.addAll(left, right)
      }
    }
  }
}
