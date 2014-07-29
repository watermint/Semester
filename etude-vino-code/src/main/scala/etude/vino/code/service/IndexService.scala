package etude.vino.code.service

import java.nio.charset.UnsupportedCharsetException
import java.nio.file.Path

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.vino.code.domain.lifecycle.{AsyncCodeRepository, AsyncFileRepository}
import etude.vino.code.domain.model.{Code, File, FileId}
import etude.pain.elasticsearch.Engine
import etude.pain.highlight.{Highlight, HighlightedCode}
import etude.pain.tika.Description

import scala.concurrent.Future
import scala.io.{BufferedSource, Source}

case class IndexService(engine: Engine,
                        followSymlink: Boolean = false) {
  val highlight: Highlight = new Highlight

  val codeRepo: AsyncCodeRepository =
    AsyncCodeRepository.withEngine(engine)

  val fileRepo: AsyncFileRepository =
    AsyncFileRepository.withEngine(engine)

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

  def asCode(file: Path, highlightedCode: HighlightedCode): Code = {
    new Code(
      fileId = new FileId(file.toString),
      code = highlightedCode.originalCode,
      highlight = highlightedCode.highlighted
    )
  }

  def asFile(file: Path, description: Description): File = {
    new File(
      fileId = new FileId(file.toString),
      contentType = description.contentType
    )
  }

  def indexText(file: Path, desc: Description)
               (implicit context: EntityIOContext[Future]) = {

    codeRepo.store(
      asCode(
        file,
        highlight(file, desc)
      )
    )
  }

  def index(file: Path)
           (implicit context: EntityIOContext[Future]) = {
    val desc = Description.ofPath(file)

    fileRepo.store(asFile(file, desc))

    if (desc.isText) {
      indexText(file, desc)
    }
  }
}
