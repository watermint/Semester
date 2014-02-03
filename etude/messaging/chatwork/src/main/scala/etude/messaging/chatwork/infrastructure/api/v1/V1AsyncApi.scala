package etude.messaging.chatwork.infrastructure.api.v1

import java.net.URI
import scala.util.{Success, Failure}
import org.json4s.{JString, JArray, JValue}
import etude.messaging.chatwork.infrastructure.api.ApiQoS
import etude.foundation.http._
import etude.foundation.domain.lifecycle.EntityIOContext
import scala.concurrent.{future, Future}
import etude.foundation.domain.lifecycle.async.AsyncEntityIO

object V1AsyncApi
  extends V1EntityIO[Future]
  with AsyncEntityIO {

  lazy val endpoint: URI = new URI("https://api.chatwork.com/")

  lazy val client: Client = Client()

  def get(path: String,
          params: List[Pair[String, String]] = List())
         (implicit context: EntityIOContext[Future]): Future[JValue] = {
    implicit val executor = getExecutionContext(context)
    future {
      ApiQoS.throttle.execute {
        client.get(
          uri = uriWithPathAndParams(path, params),
          headers = authHeaders
        ) match {
          case Failure(e) => throw e
          case Success(r) => parseResponse(r)
        }
      }
    }
  }

  def post(path: String,
           params: List[Pair[String, String]] = List(),
           data: List[Pair[String, String]] = List())
          (implicit context: EntityIOContext[Future]): Future[JValue] = {
    implicit val executor = getExecutionContext(context)
    future {
      ApiQoS.throttle.execute {
        client.post(
          uri = uriWithPathAndParams(path, params),
          formData = data,
          headers = authHeaders
        ) match {
          case Failure(e) => throw e
          case Success(r) => parseResponse(r)
        }
      }
    }
  }

  def put(path: String,
          params: List[Pair[String, String]] = List(),
          data: List[Pair[String, String]] = List())
         (implicit context: EntityIOContext[Future]): Future[JValue] = {
    implicit val executor = getExecutionContext(context)
    future {
      ApiQoS.throttle.execute {
        client.put(
          uri = uriWithPathAndParams(path, params),
          formData = data,
          headers = authHeaders
        ) match {
          case Failure(e) => throw e
          case Success(r) => parseResponse(r)
        }
      }
    }
  }

  def delete(path: String,
             params: List[Pair[String, String]] = List(),
             data: List[Pair[String, String]] = List())
            (implicit context: EntityIOContext[Future]): Future[JValue] = {
    implicit val executor = getExecutionContext(context)
    future {
      ApiQoS.throttle.execute {
        client.delete(
          uri = uriWithPathAndParams(path, params),
          formData = data,
          headers = authHeaders
        ) match {
          case Failure(e) => throw e
          case Success(r) => parseResponse(r)
        }
      }
    }
  }

  protected def uriWithPathAndParams(path: String, params: List[Pair[String, String]]): URI = {

    val endpointWithPath = endpoint.withPath(path)
    if (params.size > 0) {
      endpointWithPath.withQuery(params)
    } else {
      endpointWithPath
    }
  }

  protected def authHeaders(implicit context: EntityIOContext[Future]): List[Pair[String, String]] = {
    List(
      "X-ChatWorkToken" -> getApiToken(context)
    )
  }

  protected def parseResponse(response: Response): JValue = {
    response.statusCode match {
      case _: StatusOK => response.contentAsJson
      case _ =>
        response.contentAsJson \ "errors" match {
          case errors: JArray =>
            throw V1ApiException(
              "API Error",
              for {JString(e) <- errors.values} yield {
                e
              }
            )
          case _ =>
            throw V1ApiException(response.contentAsString)
        }
    }
  }
}
