package etude.foundation.http

import scala.language.higherKinds
import org.apache.http.client.CookieStore
import org.apache.http.impl.client.{LaxRedirectStrategy, HttpClients, CloseableHttpClient, BasicCookieStore}
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.HttpEntity
import org.apache.http.message.BasicNameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import scala.collection.JavaConverters._

trait Client[M[+A]] {

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

  def get(uri: URIContainer,
          headers: List[Pair[String, String]] = List()): M[Response]

  def post(uri: URIContainer,
           formData: List[Pair[String, String]] = List(),
           headers: List[Pair[String, String]] = List()): M[Response]

  def put(uri: URIContainer,
          formData: List[Pair[String, String]] = List(),
          headers: List[Pair[String, String]] = List()): M[Response]

  def delete(uri: URIContainer,
             formData: List[Pair[String, String]] = List(),
             headers: List[Pair[String, String]] = List()): M[Response]
}
