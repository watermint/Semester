package etude.pain.tika

import java.io.{InputStream, BufferedInputStream, FileInputStream, StringWriter}
import java.nio.file.Path

import org.apache.tika.metadata.{HttpHeaders, Metadata}
import org.apache.tika.parser.AutoDetectParser
import org.apache.tika.sax.BodyContentHandler

import scala.concurrent.{ExecutionContext, Future}

case class Description(contentType: String,
                       contentEncoding: Option[String]) {

  lazy val contentTypeWithoutParam: String = contentType.replaceAll(";.*", "")

  /**
   * @see http://tools.ietf.org/html/rfc2045#section-5
   */
  lazy val `type`: String = contentTypeWithoutParam.split("/")(0)

  lazy val subType: String = contentTypeWithoutParam.split("/")(1)

  val isText: Boolean = contentType.startsWith("text")

  val isImage: Boolean = contentType.startsWith("image")
}

object Description {
  def ofInputStream(inputStream: InputStream): Description = {
    val parser = new AutoDetectParser
    val metadata = new Metadata
    val writer = new StringWriter()
    val contentHandler = new BodyContentHandler(writer)
    val bis = new BufferedInputStream(inputStream)

    try {
      parser.parse(bis, contentHandler, metadata)
    } finally {
      inputStream.close()
    }

    Description(
      contentType = metadata.get(HttpHeaders.CONTENT_TYPE),
      contentEncoding = metadata.get(HttpHeaders.CONTENT_ENCODING) match {
        case null => None
        case mime => Some(mime)
      }
    )
  }

  def ofPath(path: Path): Description = {
    ofInputStream(new FileInputStream(path.toFile))
  }
}