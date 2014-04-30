package etude.foundation.http

import org.apache.http.client.methods.{HttpUriRequest, HttpPut, HttpPost, HttpGet}
import java.net.URI
import scala.util.{Try, Success}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.{LaxRedirectStrategy, HttpClients, CloseableHttpClient}
import etude.foundation.logging.LoggerFactory

case class SyncClient(context: ClientContext = SyncClientContext()) extends Client[Try] {
  val logger = LoggerFactory.getLogger(getClass)

  class HttpDelete(uri: URI) extends HttpPost(uri) {
    override def getMethod: String = "DELETE"
  }

  private def request(req: HttpUriRequest): Try[Response] = {
    logger.debug(req.toString)

    val httpClient: CloseableHttpClient = HttpClients.custom()
      .setDefaultCookieStore(context.cookieStore)
      .setRedirectStrategy(new LaxRedirectStrategy)
      .build()

    try {
      Success(
        Response(
          httpClient.execute(req, context.httpClientContext),
          context.httpClientContext
        )
      )
    } finally {
      httpClient.close()
    }
  }

  def get(uri: URIContainer,
          headers: Map[String, String] = Map.empty): Try[Response] = {

    val get = new HttpGet(uri)
    headers.foreach(h => get.setHeader(h._1, h._2))
    request(get)
  }

  def post(uri: URIContainer,
           formData: Map[String, String] = Map.empty,
           headers: Map[String, String] = Map.empty): Try[Response] = {

    val post = new HttpPost(uri)
    entity(formData).foreach(post.setEntity)
    headers.foreach(h => post.setHeader(h._1, h._2))
    request(post)
  }

  def postWithString(uri: URIContainer,
                     formData: String,
                     headers: Map[String, String] = Map.empty): Try[Response] = {

    val post = new HttpPost(uri)
    post.setEntity(new StringEntity(formData))
    headers.foreach(h => post.setHeader(h._1, h._2))
    request(post)
  }

  def put(uri: URIContainer,
          formData: Map[String, String] = Map.empty,
          headers: Map[String, String] = Map.empty): Try[Response] = {

    val put = new HttpPut(uri)
    entity(formData).foreach(put.setEntity)
    headers.foreach(h => put.setHeader(h._1, h._2))
    request(put)
  }

  def delete(uri: URIContainer,
             formData: Map[String, String] = Map.empty,
             headers: Map[String, String] = Map.empty): Try[Response] = {

    val delete = new HttpDelete(uri)
    entity(formData).foreach(delete.setEntity)
    headers.foreach(h => delete.setHeader(h._1, h._2))
    request(delete)
  }

}
