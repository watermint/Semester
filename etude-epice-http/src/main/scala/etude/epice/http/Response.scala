package etude.epice.http

import java.net.URI
import java.nio.charset.StandardCharsets

import etude.epice.foundation.helpers.InputStreamHelper
import InputStreamHelper._
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.{Header, HttpResponse}
import org.json4s.JValue
import org.json4s.native.JsonMethods

import scala.collection.JavaConverters._
import scala.util.Try

case class Response(statusCode: StatusCode,
                    headers: Map[String, String],
                    contentType: Option[String],
                    contentEncoding: Option[String],
                    content: Array[Byte],
                    context: HttpClientContext) {

  lazy val contentAsString: Try[String] = Try {
    new String(content, StandardCharsets.UTF_8)
  }

  lazy val contentAsJson: Try[JValue] = {
    contentAsString map {
      json =>
        JsonMethods.parse(json)
    }
  }

  def redirectLocation(): Option[URI] = {
    if (context.getRedirectLocations == null || context.getRedirectLocations.size() < 1) {
      None
    } else {
      Some(context.getRedirectLocations.asScala.last)
    }
  }
}

object Response {

  def apply(response: HttpResponse, context: HttpClientContext): Response = {
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
      content = response.getEntity.getContent.asByteArray,
      context = context
    )
  }
}