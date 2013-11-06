package etude.http

import java.io.InputStream
import org.apache.http.{Header, HttpResponse}
import etude.io.Memory._

/**
 *
 */
case class Response(statusCode: StatusCode,
                    headers: Map[String, String],
                    contentType: Option[String],
                    contentEncoding: Option[String],
                    content: InputStream) {
}

object Response {

  def apply(response: HttpResponse): Response = {
    Response(
      statusCode = StatusCode(response.getStatusLine.getStatusCode),
      headers = response.getAllHeaders.map(h => h.getName -> h.getValue).toMap,
      contentType = response.getEntity.getContentEncoding match {
        case h: Header => Some(h.getValue)
        case _ => None
      },
      contentEncoding = response.getEntity.getContentEncoding match {
        case h: Header => Some(h.getValue)
        case _ => None
      },
      content = response.getEntity.getContent.onMemory
    )
  }
}