package etude.foundation.http

import java.net.URI
import org.apache.http.client.utils.URIBuilder
import org.apache.http.message.BasicNameValuePair
import org.apache.http.NameValuePair
import scala.collection.JavaConverters._

case class URIContainer(uri: URI) {
  def withPath(path: String): URIContainer = {
    new URIContainer(
      new URIBuilder(uri).setPath(path).build
    )
  }

  def withQuery(query: (String, String)): URIContainer = {
    new URIContainer(
      new URIBuilder(uri).addParameter(query._1, query._2).build
    )
  }

  def withQuery(query: Map[String, String]): URIContainer = {
    val params: List[NameValuePair] = query.map {
      q =>
        new BasicNameValuePair(q._1, q._2)
    }.toList
    new URIContainer(
      new URIBuilder(uri).addParameters(params.asJava).build
    )
  }
}
