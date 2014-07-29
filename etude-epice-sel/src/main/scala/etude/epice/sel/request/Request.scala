package etude.epice.sel.request

import etude.epice.sel.client.Context
import etude.epice.sel.response.Response

import scala.util.Try

trait Request {
  val verb: Verb

  val context: Context

  def withPayload(payload: Payload): Request = ???

  def withQueryString(key: String, value: String): Request = withQueryString(Map(key -> value))

  def withQueryString(keyValue: Map[String, String]): Request = ???

  def withHeader(header: Header): Request = withHeaders(Seq(header))

  def withHeaders(headers: Seq[Header]): Request = ???

  def execute(): Try[Response]
}
