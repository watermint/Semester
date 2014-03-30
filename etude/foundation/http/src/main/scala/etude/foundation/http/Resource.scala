package etude.foundation.http

import scala.util.Try

case class Resource(uri: URIContainer) {
  def get: Try[Response] = SyncClient().get(uri)

  def post(formData: List[Pair[String, String]] = List()): Try[Response] = SyncClient().post(uri, formData)
}
