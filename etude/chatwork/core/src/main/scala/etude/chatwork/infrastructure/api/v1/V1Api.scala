package etude.chatwork.infrastructure.api.v1

import java.net.URI
import scala.util.{Success, Failure, Try}
import org.json4s.{JString, JArray, JValue}
import etude.chatwork.infrastructure.api.ApiQoS
import etude.foundation.http._

object V1Api {
  lazy val endpoint: URI = new URI("https://api.chatwork.com/")

  lazy val client: Client = Client()

  def get(path: String,
          params: List[Pair[String, String]] = List())
         (implicit token: V1AuthToken): Try[JValue] = {

    ApiQoS.throttle.execute {
      client.get(
        uri = uriWithPathAndParams(path, params),
        headers = authHeaders
      ) match {
        case Failure(e) => Failure(e)
        case Success(r) => parseResponse(r)
      }
    }
  }

  def post(path: String,
           params: List[Pair[String, String]] = List(),
           data: List[Pair[String, String]] = List())
          (implicit token: V1AuthToken): Try[JValue] = {

    ApiQoS.throttle.execute {
      client.post(
        uri = uriWithPathAndParams(path, params),
        formData = data,
        headers = authHeaders
      ) match {
        case Failure(e) => Failure(e)
        case Success(r) => parseResponse(r)
      }
    }
  }

  def put(path: String,
          params: List[Pair[String, String]] = List(),
          data: List[Pair[String, String]] = List())
         (implicit token: V1AuthToken): Try[JValue] = {

    ApiQoS.throttle.execute {
      client.put(
        uri = uriWithPathAndParams(path, params),
        formData = data,
        headers = authHeaders
      ) match {
        case Failure(e) => Failure(e)
        case Success(r) => parseResponse(r)
      }
    }
  }

  def delete(path: String,
             params: List[Pair[String, String]] = List(),
             data: List[Pair[String, String]] = List())
            (implicit token: V1AuthToken): Try[JValue] = {

    ApiQoS.throttle.execute {
      client.delete(
        uri = uriWithPathAndParams(path, params),
        formData = data,
        headers = authHeaders
      ) match {
        case Failure(e) => Failure(e)
        case Success(r) => parseResponse(r)
      }
    }
  }

  private def uriWithPathAndParams(path: String, params: List[Pair[String, String]]): URI = {

    val endpointWithPath = endpoint.withPath(path)
    if (params.size > 0) {
      endpointWithPath.withQuery(params)
    } else {
      endpointWithPath
    }
  }

  private def authHeaders(implicit authToken: V1AuthToken): List[Pair[String, String]] = {
    List(
      "X-ChatWorkToken" -> authToken.token
    )
  }

  private def parseResponse(response: Response): Try[JValue] = {
    response.statusCode match {
      case _: StatusOK => Success(response.contentAsJson)
      case _ =>
        response.contentAsJson \ "errors" match {
          case errors: JArray =>
            Failure(V1ApiException(
              "API Error",
              for {JString(e) <- errors.values} yield {
                e
              }
            ))
          case _ =>
            Failure(V1ApiException(response.contentAsString))
        }
    }
  }
}
