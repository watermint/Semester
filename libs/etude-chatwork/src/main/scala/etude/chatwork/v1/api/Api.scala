package etude.chatwork.v1.api

import etude.http._
import etude.qos.Throttle
import java.net.URI
import scala.util.{Success, Failure, Try}
import org.json4s.{JString, JArray, JValue}

object Api {
  lazy val QOS_RETRY_DURATION_OF_FAILURE = 3

  lazy val throttle: Throttle = Throttle(maxQueryPerSecond = 0.5, randomWaitRangeSeconds = 4)

  lazy val endpoint: URI = new URI("https://api.chatwork.com/")

  lazy val client: Client = Client()

  def get(path: String,
          params: List[Pair[String, String]] = List())
         (implicit token: AuthToken): Try[JValue] = {

    client.get(
      uri = uriWithPathAndParams(path, params),
      headers = authHeaders
    ) match {
      case Left(e) => Failure(e)
      case Right(r) => parseResponse(r)
    }
  }

  def post(path: String,
           params: List[Pair[String, String]] = List(),
           data: List[Pair[String, String]] = List())
          (implicit token: AuthToken): Try[JValue] = {

    client.post(
      uri = uriWithPathAndParams(path, params),
      formData = data,
      headers = authHeaders
    ) match {
      case Left(e) => Failure(e)
      case Right(r) => parseResponse(r)
    }
  }

  def put(path: String,
          params: List[Pair[String, String]] = List(),
          data: List[Pair[String, String]] = List())
         (implicit token: AuthToken): Try[JValue] = {

    client.post(
      uri = uriWithPathAndParams(path, params),
      formData = data,
      headers = authHeaders
    ) match {
      case Left(e) => Failure(e)
      case Right(r) => parseResponse(r)
    }
  }

  def delete(path: String,
             params: List[Pair[String, String]] = List(),
             data: List[Pair[String, String]] = List())
            (implicit token: AuthToken): Try[JValue] = {

    client.post(
      uri = uriWithPathAndParams(path, params),
      formData = data,
      headers = authHeaders
    ) match {
      case Left(e) => Failure(e)
      case Right(r) => parseResponse(r)
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

  private def authHeaders(implicit authToken: AuthToken): List[Pair[String, String]] = {
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
            Failure(ApiException(
              "API Error",
              for { JString(e) <- errors.values } yield { e }
            ))
          case _ =>
            Failure(ApiException(response.contentAsString))
        }
    }
  }
}
