package stenographer.desktop

import fextile._
import scalafx.application.JFXApp
import scalafx.scene.Node
import scalafx.scene.control.{PasswordField, TextField, Label}
import scalafx.scene.text.TextAlignment
import scalafx.geometry.{HPos, Pos}

object Mock extends JFXApp {
  lazy val apiKeyField = new TextField {}

  lazy val apiOrgIdField = new TextField {}

  lazy val apiEmailField = new TextField {}

  lazy val apiPasswordField = new PasswordField {}

  def labelControl(labelText: String, field: TextField): Node = {
    new Label {
      textAlignment = TextAlignment.RIGHT
      text = labelText
      labelFor = field
    }
  }

  def requiredLabel(): Node = {
    new Label {
      text = "* required"
      styleClass = Seq("text-warning")
    }
  }

  def gridRow = new GridRow {
    (1 to 15).foreach {
      i =>
        add(new Label {
          text = s"c${i}r0"
          style = s"-fx-background-color: #${i.toLong.toHexString}04040"
        }, GridSpanMedium(gridSpan = i % 2 + 1))
    }
  }

  lazy val connectButton = new ButtonPrimary {
    alignment = Pos.CENTER
    text = "Connect"
    onAction = event {
      e =>
        basePane.pushNode(connectValidationPane)
    }
  }

  lazy val authPane = new GridRow {
    add(new H1 {
      text = "Connect to ChatWork"
    }, xs12, sm12, md10.offset(1), lg10.offset(1)).hpos(HPos.CENTER)
    add(labelControl("API Key", apiKeyField), xs12, sm4, md3.offset(1), lg3.offset(1)).hpos(HPos.RIGHT)
    add(apiKeyField, xs12, sm4, md3, lg3)
    add(requiredLabel(), xs12, sm4, md3, lg3)
    add(connectButton, xs12, sm4.offset(4), md4.offset(4), lg3.offset(3))
  }

  lazy val connectValidationPane = new VContainer {
    content = Seq(
      new H1 {
        text = "In progress..."
      },
      new ButtonPrimary {
        text = "Primary"
        onAction = event {
          e =>
            basePane.pushNode(gridRow)
        }
      },
      new ButtonSuccess {
        text = "Success"
      },
      new ButtonInfo {
        text = "Info"
      },
      new ButtonWarning {
        text = "warning"
      },
      new ButtonDanger {
        text = "danger"
      },
      new Button {
        text = "Next"
        onAction = event {
          e =>
            basePane.pushNode(yayPane)
        }
      },
      new Button {
        text = "Back"
        onAction = event {
          e =>
            basePane.popNode()
        }
      }
    )
  }

  lazy val yayPane = new VContainer {
    content = Seq(
      new H1 {
        text = "Yay!"
      },
      new Button {
        text = "Back"
        onAction = event {
          e =>
            basePane.popNode()
        }
      }
    )
  }

  lazy val basePane: PushNodePane = new PushNodePane(authPane)

  stage = new JFXApp.PrimaryStage {
    title = "Fextile"
    width = 800
    height = 600
    scene = new Scene {
      root = basePane
    }
  }
}
