package etude.foundation.http

import org.apache.http.client.methods.{HttpUriRequest, HttpPut, HttpPost, HttpGet}
import java.net.URI
import scala.util.{Try, Success}

case class SyncClient(context: ClientContext = SyncClientContext()) extends Client[Try] {
  class HttpDelete(uri: URI) extends HttpPost(uri) {
    override def getMethod: String = "DELETE"
  }

  private def request(req: HttpUriRequest): Try[Response] = {
    Success(
      Response(
        context.httpClient.execute(req, context.httpClientContext),
        context.httpClientContext
      )
    )
  }

  def get(uri: URIContainer,
          headers: List[Pair[String, String]] = List()): Try[Response] = {

    val get = new HttpGet(uri)
    headers.foreach(h => get.setHeader(h._1, h._2))
    request(get)
  }

  def post(uri: URIContainer,
           formData: List[Pair[String, String]] = List(),
           headers: List[Pair[String, String]] = List()): Try[Response] = {

    val post = new HttpPost(uri)
    entity(formData).foreach(post.setEntity)
    headers.foreach(h => post.setHeader(h._1, h._2))
    request(post)
  }

  def put(uri: URIContainer,
          formData: List[Pair[String, String]] = List(),
          headers: List[Pair[String, String]] = List()): Try[Response] = {

    val put = new HttpPut(uri)
    entity(formData).foreach(put.setEntity)
    headers.foreach(h => put.setHeader(h._1, h._2))
    request(put)
  }

  def delete(uri: URIContainer,
             formData: List[Pair[String, String]] = List(),
             headers: List[Pair[String, String]] = List()): Try[Response] = {

    val delete = new HttpDelete(uri)
    entity(formData).foreach(delete.setEntity)
    headers.foreach(h => delete.setHeader(h._1, h._2))
    request(delete)
  }

}
