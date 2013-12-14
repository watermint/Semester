package etude.foundation.http

import org.apache.http.client.CookieStore
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.{HttpUriRequest, HttpPut, HttpPost, HttpGet}
import org.apache.http.HttpEntity
import org.apache.http.impl.client.{HttpClients, CloseableHttpClient, BasicCookieStore}
import org.apache.http.message.BasicNameValuePair
import scala.collection.JavaConverters._
import java.net.URI
import scala.util.{Failure, Try, Success}

case class Client() {
  val cookieStore: CookieStore = new BasicCookieStore()

  def httpClient: CloseableHttpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build()

  class HttpDelete(uri: URI) extends HttpPost(uri) {
    override def getMethod: String = "DELETE"
  }

  private def request(req: HttpUriRequest): Try[Response] = {
    val client = httpClient
    try {
      Success(Response(client.execute(req)))
    } catch {
      case e: Exception => Failure(e)
    } finally {
      client.close()
    }
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

  def entity(formData: List[Pair[String, String]]): Option[HttpEntity] = {
    if (formData.size < 1) {
      None
    } else {
      val formNvp = formData.map {
        f =>
          new BasicNameValuePair(f._1, f._2)
      }
      Some(new UrlEncodedFormEntity(formNvp.asJava))
    }
  }
}
