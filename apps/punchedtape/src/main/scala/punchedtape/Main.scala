package punchedtape

import etude.file.{File, Home, Dir}
import scala.io.Source
import scala.pickling.json._

case class Main(auth: Auth, tape: Tape) {
  def play(): Unit = {
    tape.play(auth.session)
  }
}

object Main {
  lazy val home: Dir = Home.user.resolveDir(".punchedtape")

  lazy val authFile: File = home.resolveFile("auth.json")

  lazy val tapeFile: File = home.resolveFile("tape.json")

  lazy val auth = Source.fromFile(authFile.javaFile).getLines().toList.mkString.unpickle[Auth]

  lazy val tape = Source.fromFile(tapeFile.javaFile).getLines().toList.mkString.unpickle[Tape]

  def usage(): Unit = {
    val description =
      """
        |1. Place your account information on your $HOME/.punchedtape/auth.json with format like below.
        |Note auth.json should not be readable from other person. Change permission to 0600.
        |
        |1-1. Your account is on chatwork.com
        |
        |{
        |  "tpe": "punchedtape.Auth",
        |  "email": "<your email address>",
        |  "password": "<your password>",
        |  "orgId": {
        |    "tpe": "scala.None.type"
        |  }
        |}
        |
        |1-2. Your account is on kddi chatwork
        |
        |{
        |  "tpe": "punchedtape.Auth",
        |  "email": "<your email address>",
        |  "password": "<your password>",
        |  "orgId": {
        |    "tpe": "scala.Some[java.lang.String]",
        |    "x": "<your organization id>"
        |  }
        |}
        |
        |2. Place your regular tasks on your $HOME/.punchedtape/tape.json
        |
        |{
        |  "tpe": "punchedtape.Tape",
        |  "punches": {
        |    "tpe": "scala.collection.immutable.$colon$colon[punchedtape.Punch]",
        |    "elems": [
        |      {
        |        "tpe": "punchedtape.MarkAsRead",
        |        "roomId": "<your chat room id>"
        |      },
        |      {
        |        "tpe": "punchedtape.MarkAsRead",
        |        "roomId": "<your chat room id>"
        |      }
        |    ]
        |  }
        |}
        |
      """.stripMargin

    println(description)
  }

  def main(args: Array[String]): Unit = {
    if (home.exists && authFile.exists && tapeFile.exists) {
      Main(auth, tape).play()
    } else {
      usage()
    }
  }
}
