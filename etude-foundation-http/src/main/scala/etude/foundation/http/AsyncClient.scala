package etude.foundation.http

import etude.foundation.utility.Converter

import scala.concurrent.Future

case class AsyncClient(context: AsyncClientContext) extends Client[Future] {
  private val syncClient = SyncClient(context)

  def delete(uri: URIContainer,
             formData: Map[String, String] = Map.empty,
             headers: Map[String, String] = Map.empty): Future[Response] = {
    implicit val executor = context.executionContext
    Future.successful(
      Converter.unwrapTry(syncClient.delete(uri, formData, headers))
    )
  }

  def put(uri: URIContainer,
          formData: Map[String, String] = Map.empty,
          headers: Map[String, String] = Map.empty): Future[Response] = {
    implicit val executor = context.executionContext
    Future.successful(
      Converter.unwrapTry(syncClient.put(uri, formData, headers))
    )
  }

  def post(uri: URIContainer,
           formData: Map[String, String] = Map.empty,
           headers: Map[String, String] = Map.empty): Future[Response] = {
    implicit val executor = context.executionContext
    Future.successful(
      Converter.unwrapTry(syncClient.post(uri, formData, headers))
    )
  }

  def postWithString(uri: URIContainer,
                     formData: String,
                     headers: Map[String, String] = Map.empty): Future[Response] = {
    implicit val executor = context.executionContext
    Future.successful(
      Converter.unwrapTry(syncClient.postWithString(uri, formData, headers))
    )
  }

  def get(uri: URIContainer,
          headers: Map[String, String] = Map.empty): Future[Response] = {
    implicit val executor = context.executionContext
    Future.successful(
      Converter.unwrapTry(syncClient.get(uri, headers))
    )
  }
}
