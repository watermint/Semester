package etude.foundation.http

import scala.concurrent.{Future, future}
import etude.foundation.utility.Converter

case class AsyncClient(context: AsyncClientContext) extends Client[Future] {
  private val syncClient = SyncClient(context)

  def delete(uri: URIContainer,
             formData: List[(String, String)] = List.empty,
             headers: List[(String, String)] = List.empty): Future[Response] = {
    implicit val executor = context.executionContext
    future {
      Converter.unwrapTry(syncClient.delete(uri, formData, headers))
    }
  }

  def put(uri: URIContainer,
          formData: List[(String, String)] = List.empty,
          headers: List[(String, String)] = List.empty): Future[Response] = {
    implicit val executor = context.executionContext
    future {
      Converter.unwrapTry(syncClient.put(uri, formData, headers))
    }
  }

  def post(uri: URIContainer,
           formData: List[(String, String)] = List.empty,
           headers: List[(String, String)] = List.empty): Future[Response] = {
    implicit val executor = context.executionContext
    future {
      Converter.unwrapTry(syncClient.post(uri, formData, headers))
    }
  }

  def postWithString(uri: URIContainer,
                     formData: String,
                     headers: List[(String, String)] = List.empty): Future[Response] = {
    implicit val executor = context.executionContext
    future {
      Converter.unwrapTry(syncClient.postWithString(uri, formData, headers))
    }
  }

  def get(uri: URIContainer,
          headers: List[(String, String)] = List.empty): Future[Response] = {
    implicit val executor = context.executionContext
    future {
      Converter.unwrapTry(syncClient.get(uri, headers))
    }
  }
}
