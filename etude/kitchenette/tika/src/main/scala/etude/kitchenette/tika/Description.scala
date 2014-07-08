package etude.kitchenette.tika

import java.io.{BufferedInputStream, FileInputStream, StringWriter}
import java.nio.file.Path

import org.apache.tika.metadata.{HttpHeaders, Metadata}
import org.apache.tika.parser.AutoDetectParser
import org.apache.tika.sax.BodyContentHandler

import scala.concurrent.{ExecutionContext, Future}

case class Description(path: Path,
                       contentType: String,
                       contentEncoding: String) {

  val isText: Boolean = contentType.startsWith("text")

  val isImage: Boolean = contentType.startsWith("image")
}

object Description {
  def ofPath(path: Path)(implicit ctx: ExecutionContext): Future[Description] = Future {
    val parser = new AutoDetectParser
    val metadata = new Metadata
    val writer = new StringWriter()
    val contentHandler = new BodyContentHandler(writer)
    val fis = new FileInputStream(path.toFile)
    val bis = new BufferedInputStream(fis)

    try {
      parser.parse(bis, contentHandler, metadata)
    } finally {
      fis.close()
    }

    Description(
      path = path,
      contentType = metadata.get(HttpHeaders.CONTENT_TYPE),
      contentEncoding = metadata.get(HttpHeaders.CONTENT_ENCODING)
    )
  }
}