package etude.vino.chatwork.ui

import scalafx.geometry.Insets

object UIStyles {
  val avatarThumbnail = 30
  val accountNameWidth = avatarThumbnail * 3
  val spacingWidth = 5
  val paddingPixel = spacingWidth * 2
  val paddingInsets = Insets(spacingWidth)

  private val headerStyle =
    """
      |  -fx-font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
      |  -fx-font-weight: 500;
    """.stripMargin

  val h1Style =
    s"""
      |$headerStyle
      |  -fx-padding: 20px 0 10px 0;
      |  -fx-font-size: 36px;
    """.stripMargin

  val h2Style =
    s"""
     |$headerStyle
     |  -fx-padding: 20px 0 10px 0;
     |  -fx-font-size: 30px;
   """.stripMargin

  val h3Style =
    s"""
     |$headerStyle
     |  -fx-padding: 20px 0 10px 0;
     |  -fx-font-size: 24px;
   """.stripMargin

  val h4Style =
    s"""
     |$headerStyle
     |  -fx-padding: 10px 0 10px 0;
     |  -fx-font-size: 18px;
   """.stripMargin

  val h5Style =
    s"""
     |$headerStyle
     |  -fx-padding: 10px 0 10px 0;
     |  -fx-font-size: 14px;
   """.stripMargin

  val h6Style =
    s"""
     |$headerStyle
     |  -fx-padding: 10px 0 10px 0;
     |  -fx-font-size: 12px;
   """.stripMargin

}
