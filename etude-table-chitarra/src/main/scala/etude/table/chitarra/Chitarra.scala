package etude.table.chitarra

import java.nio.file.{Paths, Files}

import scopt.OptionParser

import scala.io.Source
import scala.xml.{XML, Node, Elem}

case class Chitarra(source: Elem,
                    preferredChildCount: Int = 15,
                    maxHeadingDepth: Int = 6) {
  val title: Option[String] = {
    (source \ "head" \ "title").lastOption collect {
      case s => s.text
    }
  }

  val body: Node = (source \ "body").last

  lazy val bodyDepth: Int = depth(body)

  lazy val aveBodyDepth: Double = aveDepth(body)

  lazy val headingDepth: Int = Seq(maxHeadingDepth, Math.ceil(aveBodyDepth).toInt + 1).min

  lazy val bodyMarkdown: String = markdown(body)

  def aveDepth(n: Node, currentDepth: Int = 1): Double = {
    val count = n.child.size
    if (count == 0) {
      currentDepth
    } else {
      n.child.map(c => aveDepth(c, currentDepth + 1)).sum / count.toDouble
    }
  }

  def depth(n: Node, currentDepth: Int = 1): Int = {
    if (n.child.size == 0) {
      currentDepth
    } else {
      n.child.map(c => depth(c, currentDepth + 1)).max
    }
  }

  def childCount(n: Node): Int = {
    val count = n.child.size
    if (count == 0) {
      0
    } else {
      n.child.map(c => childCount(c)).sum + count
    }
  }

  def markdown(n: Node, currentDepth: Int = 0): String = {
    Seq(
      n.attribute("text").lastOption.collect { case t => line(currentDepth, t.text)}.getOrElse(""),
      (n \ "outline").map {
        outline =>
          markdown(outline, currentDepth + 1)
      }.mkString
    ).mkString("\n")
  }

  def line(depth: Int, text: String): String = {
    if (depth > headingDepth) {
      bullet(depth - headingDepth, text)
    } else {
      heading(depth, text)
    }
  }

  def heading(level: Int, text: String): String = "\n" + "#" * level + " " + text + "\n"

  def bullet(level: Int, text: String): String = "    " * (level - 1) + "* " + text
}

object Chitarra {

  case class Operation(inputFile: Option[String] = None)

  def fromFile(file: String): Chitarra = {
    Chitarra(
      XML.loadString(Source.fromFile(file).getLines().mkString)
    )
  }

  val argParser = new OptionParser[Operation]("chitarra") {
    head("chitarra", "OPML to Markdown converter")

    opt[String]('i', "input") action {
      case (i, o) =>
        o.copy(inputFile = Some(i))
    } validate {
      i =>
        Files.exists(Paths.get(i)) match {
          case true => success
          case _ => failure(s"Input file not found at: $i")
        }
    } text {
      "Input file name"
    } required()
  }

  def main(args: Array[String]) {
    argParser.parse(args, Operation()) map {
      o =>
        o.inputFile.foreach {
          f =>
            val c = fromFile(f)
            println(c.title.getOrElse(""))
            println("----")

            println(c.bodyMarkdown)
        }
    }
  }
}
