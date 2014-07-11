package etude.adapter.chatwork.domain.infrastructure.api.v1

import java.net.URI

import etude.domain.core.lifecycle.EntityIOContext
import etude.domain.core.lifecycle.async.AsyncEntityIO
import etude.foundation.http._
import org.json4s.{JArray, JString, JValue}

import scala.concurrent.Future

object V1AsyncApi
  extends V1EntityIO[Future]
  with AsyncEntityIO {

  lazy val endpoint: URI = new URI("https://api.chatwork.com/")

  def get(path: String,
          params: Map[String, String] = Map.empty)
         (implicit context: EntityIOContext[Future]): Future[JValue] = {
    implicit val executor = getExecutionContext(context)
    val client: Client[Future] = AsyncClient(AsyncClientContext(executor))

    V1ApiQoS.throttle.execute {
      client.get(
        uri = uriWithPathAndParams(path, params),
        headers = authHeaders
      ).map(parseResponse)
    }
  }

  def post(path: String,
           params: Map[String, String] = Map.empty,
           data: Map[String, String] = Map.empty)
          (implicit context: EntityIOContext[Future]): Future[JValue] = {
    implicit val executor = getExecutionContext(context)
    val client: Client[Future] = AsyncClient(AsyncClientContext(executor))

    V1ApiQoS.throttle.execute {
      client.post(
        uri = uriWithPathAndParams(path, params),
        formData = data,
        headers = authHeaders
      ).map(parseResponse)
    }
  }

  def put(path: String,
          params: Map[String, String] = Map.empty,
          data: Map[String, String] = Map.empty)
         (implicit context: EntityIOContext[Future]): Future[JValue] = {
    implicit val executor = getExecutionContext(context)
    val client: Client[Future] = AsyncClient(AsyncClientContext(executor))

    V1ApiQoS.throttle.execute {
      client.put(
        uri = uriWithPathAndParams(path, params),
        formData = data,
        headers = authHeaders
      ).map(parseResponse)
    }
  }

  def delete(path: String,
             params: Map[String, String] = Map.empty,
             data: Map[String, String] = Map.empty)
            (implicit context: EntityIOContext[Future]): Future[JValue] = {
    implicit val executor = getExecutionContext(context)
    val client: Client[Future] = AsyncClient(AsyncClientContext(executor))

    V1ApiQoS.throttle.execute {
      client.delete(
        uri = uriWithPathAndParams(path, params),
        formData = data,
        headers = authHeaders
      ).map(parseResponse)
    }
  }

  protected def uriWithPathAndParams(path: String, params: Map[String, String]): URI = {
    val endpointWithPath = endpoint.withPath(path)
    if (params.size > 0) {
      endpointWithPath.withQuery(params)
    } else {
      endpointWithPath
    }
  }

  protected def authHeaders(implicit context: EntityIOContext[Future]): Map[String, String] = {
    Map(
      "X-ChatWorkToken" -> getApiToken(context)
    )
  }

  protected def parseResponse(response: Response): JValue = {
    response.statusCode match {
      case _: StatusOK => response.contentAsJson.get
      case _ =>
        response.contentAsJson.get \ "errors" match {
          case errors: JArray =>
            throw V1ApiException(
              "API Error",
              for {JString(e) <- errors.values} yield {
                e
              }
            )
          case _ =>
            throw V1ApiException(response.contentAsString.get)
        }
    }
  }
}
