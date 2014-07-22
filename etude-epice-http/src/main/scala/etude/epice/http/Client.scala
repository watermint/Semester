package etude.epice.http

import org.apache.http.HttpEntity
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.message.BasicNameValuePair

import scala.collection.JavaConverters._
import scala.language.higherKinds

trait Client[M[+A]] {

  def entity(formData: Map[String, String]): Option[HttpEntity] = {
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
          headers: Map[String, String] = Map.empty): M[Response]

  def post(uri: URIContainer,
           formData: Map[String, String] = Map.empty,
           headers: Map[String, String] = Map.empty): M[Response]

  def put(uri: URIContainer,
          formData: Map[String, String] = Map.empty,
          headers: Map[String, String] = Map.empty): M[Response]

  def delete(uri: URIContainer,
             formData: Map[String, String] = Map.empty,
             headers: Map[String, String] = Map.empty): M[Response]
}
