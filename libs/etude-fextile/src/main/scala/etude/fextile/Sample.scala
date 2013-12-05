package etude.fextile

import scalafx.application.JFXApp
import scalafx.scene._
import scalafx.scene.layout._
import scalafx.scene.control._
import Fextile._

object Sample extends JFXApp {
  lazy val sample = new VBox {
    styleClass = Seq("container")
    content = Seq(
      new Label {
        text = "page-header, Heading 1"
        styleClass = Seq("page-header", "h1")
      },
      new Label {
        text = "Lorem ipsum dolor sit amet, quo nibh illud prodesset ne, erant decore eruditi sit et, graeci singulis mandamus cu sed. Mei eu etiam phaedrum mnesarchum, ius libris meliore vivendo eu. Ei eos eripuit delectus sadipscing, ex eos fabulas detraxit, eu vix vidit percipitur voluptatibus. Pri alii nulla ea, est cu tincidunt referrentur, pri mucius corpora scaevola eu. An eos tation primis omnesque, quo inani evertitur cu."
        wrapText = true
        styleClass = Seq("text-muted")
      },
      new H2 {
        text = "Heading 2"
      },
      new Label {
        text = "Lorem ipsum dolor sit amet, quo nibh illud prodesset ne, erant decore eruditi sit et, graeci singulis mandamus cu sed. Mei eu etiam phaedrum mnesarchum, ius libris meliore vivendo eu. Ei eos eripuit delectus sadipscing, ex eos fabulas detraxit, eu vix vidit percipitur voluptatibus. Pri alii nulla ea, est cu tincidunt referrentur, pri mucius corpora scaevola eu. An eos tation primis omnesque, quo inani evertitur cu."
        wrapText = true
      },
      new H3 {
        text = "Heading 3"
      },
      new Label {
        text = "Lorem ipsum dolor sit amet, quo nibh illud prodesset ne, erant decore eruditi sit et, graeci singulis mandamus cu sed. Mei eu etiam phaedrum mnesarchum, ius libris meliore vivendo eu. Ei eos eripuit delectus sadipscing, ex eos fabulas detraxit, eu vix vidit percipitur voluptatibus. Pri alii nulla ea, est cu tincidunt referrentur, pri mucius corpora scaevola eu. An eos tation primis omnesque, quo inani evertitur cu."
        wrapText = true
        styleClass = Seq("text-success")
      },
      new H3 {
        text = "Alert"
      },
      new HBox {
        spacing = 7
        content = Seq(
          new AlertSuccess {
            text = "alert alert-success"
          },
          new AlertInfo {
            text = "alert alert-info"
          },
          new AlertWarning {
            text = "alert alert-warning"
          },
          new AlertDanger {
            text = "alert alert-danger"
          }
        )
      }
    )
  }

  stage = new JFXApp.PrimaryStage {
    title = "Fextile"
    width = 800
    height = 600
    scene = new Scene {
      stylesheets.add("fextile.css")
      root = sample
    }
  }
}
