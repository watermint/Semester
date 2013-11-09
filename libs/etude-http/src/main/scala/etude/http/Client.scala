package etude.http

import org.apache.http.client.CookieStore
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.{HttpPost, HttpGet}
import org.apache.http.HttpEntity
import org.apache.http.impl.client.{HttpClients, CloseableHttpClient, BasicCookieStore}
import org.apache.http.message.BasicNameValuePair
import scala.collection.JavaConverters._

case class Client() {
  val cookieStore: CookieStore = new BasicCookieStore()

  def httpClient: CloseableHttpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build()

  def get(uri: URIContainer): Either[Exception, Response] = {
    val client = httpClient
    try {
      Right(Response(client.execute(new HttpGet(uri))))
    } catch {
      case e: Exception => Left(e)
    } finally {
      client.close()
    }
  }

  def post(uri: URIContainer, formData: List[Pair[String, String]] = List()): Either[Exception, Response] = {
    val client = httpClient
    try {
      val post = new HttpPost(uri)
      entity(formData).foreach(post.setEntity)
      Right(Response(client.execute(post)))
    } catch {
      case e: Exception => Left(e)
    } finally {
      client.close()
    }
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
