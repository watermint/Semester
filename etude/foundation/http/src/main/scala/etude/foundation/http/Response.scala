package etude.foundation.http

import org.apache.http.{Header, HttpResponse}
import org.json4s.JValue
import org.json4s.native.JsonMethods
import etude.foundation.utility.io.Memory._
import scala.Some

case class Response(statusCode: StatusCode,
                    headers: Map[String, String],
                    contentType: Option[String],
                    contentEncoding: Option[String],
                    content: Array[Byte]) {

  lazy val contentAsString: String = new String(content)

  lazy val contentAsJson: JValue = JsonMethods.parse(contentAsString)
}

object Response {

  def apply(response: HttpResponse): Response = {
    Response(
      statusCode = StatusCode(response.getStatusLine.getStatusCode),
      headers = response.getAllHeaders.map(h => h.getName -> h.getValue).toMap,
      contentType = response.getEntity.getContentType match {
        case h: Header => Some(h.getValue)
        case _ => None
      },
      contentEncoding = response.getEntity.getContentEncoding match {
        case h: Header => Some(h.getValue)
        case _ => None
      },
      content = response.getEntity.getContent.asByteArray
    )
  }
}