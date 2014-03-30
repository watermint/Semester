package etude.foundation.http

import scala.concurrent.Future
import etude.foundation.utility.Converter

case class AsyncClient() extends Client[Future] {
  private val syncClient = SyncClient()

  def delete(uri: URIContainer, formData: List[(String, String)], headers: List[(String, String)]): Future[Response] = {
    Converter.tryToFuture(syncClient.delete(uri, formData, headers))
  }

  def put(uri: URIContainer, formData: List[(String, String)], headers: List[(String, String)]): Future[Response] = {
    Converter.tryToFuture(syncClient.put(uri, formData, headers))
  }

  def post(uri: URIContainer, formData: List[(String, String)], headers: List[(String, String)]): Future[Response] = {
    Converter.tryToFuture(syncClient.post(uri, formData, headers))
  }

  def get(uri: URIContainer, headers: List[(String, String)]): Future[Response] = {
    Converter.tryToFuture(syncClient.get(uri, headers))
  }
}
