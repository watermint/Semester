package etude.foundation.http

import scala.util.Try

case class Resource(uri: URIContainer) {
  def get: Try[Response] = SyncClient().get(uri)

  def post(formData: Map[String, String] = Map.empty): Try[Response] = SyncClient().post(uri, formData)
}
