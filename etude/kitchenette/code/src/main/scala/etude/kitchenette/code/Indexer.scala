package etude.kitchenette.code

import java.nio.charset.UnsupportedCharsetException
import java.nio.file.Path

import etude.kitchenette.elasticsearch.Engine
import etude.kitchenette.highlight.{HighlightedCode, Highlight}
import etude.kitchenette.tika.Description

import scala.io.{BufferedSource, Source}

case class Indexer(engine: Engine) {
  val highlight: Highlight = new Highlight

  def highlight(file: Path, desc: Description): HighlightedCode = {
    val source: BufferedSource = desc.contentEncoding match {
      case Some(enc) =>
        try {
          Source.fromFile(file.toFile, enc)
        } catch {
          case _: UnsupportedCharsetException =>
            Source.fromFile(file.toFile)
        }
      case None =>
        Source.fromFile(file.toFile)
    }

    highlight.highlight(source.getLines().mkString("\n"))
  }

  def indexText(file: Path, desc: Description) = {
  }

  def index(file: Path) = {
    val desc = Description.ofPath(file)
    if (desc.isText) {
      indexText(file, desc)
    }
  }
}
