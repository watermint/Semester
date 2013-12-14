package etude.foundation.http

import scala.util.Try

case class Resource(uri: URIContainer) {
  def get: Try[Response] = Client().get(uri)

  def post(formData: List[Pair[String, String]] = List()): Try[Response] = Client().post(uri, formData)
}
