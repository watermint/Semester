package stenographer.desktop

import etude.fextile._
import scalafx.application.JFXApp
import scalafx.scene.{Parent, Node, Scene}
import scalafx.scene.layout._
import scalafx.scene.control.{PasswordField, TextField, Label}
import scalafx.scene.text.TextAlignment
import scalafx.geometry.{Insets, Pos}

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
    (1 to 24).foreach {
      i =>
        add(new Label { text = s"c${i}r0"; style = s"-fx-background-color: #${i.toLong.toHexString}04040" }, GridSpanMedium(span = i % 2 + 1))
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

  lazy val authPane: Parent = new VContainer {
    alignment = Pos.CENTER
    content = Seq(
      new H1 {
        alignment = Pos.CENTER
        textAlignment = TextAlignment.CENTER
        text = "Connect to ChatWork"
      },
      new GridPane {
        alignment = Pos.CENTER
        hgap = 8
        vgap = 8
        addRow(0, labelControl("API Key", apiKeyField), apiKeyField, requiredLabel())
        addRow(1, labelControl("Organization ID", apiOrgIdField), apiOrgIdField)
        addRow(2, labelControl("Email", apiEmailField), apiEmailField)
        addRow(3, labelControl("Password", apiPasswordField), apiPasswordField)
      },
      connectButton
    )
  }

  lazy val connectValidationPane: Parent = new VContainer {
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

  def yayPane: Parent = new VContainer {
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
      stylesheets.add("fextile.css")
      root = gridRow
    }
  }
}
