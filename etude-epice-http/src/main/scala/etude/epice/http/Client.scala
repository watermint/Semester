package etude.epice.http

import java.io.InputStream
import java.net.URI
import java.util.concurrent.locks.ReentrantLock

import etude.epice.logging.LoggerFactory
import org.apache.http.HttpEntity
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.{HttpGet, HttpPost, HttpPut, HttpUriRequest}
import org.apache.http.entity.StringEntity
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.impl.DefaultConnectionReuseStrategy
import org.apache.http.impl.client._
import org.apache.http.message.BasicNameValuePair

import scala.collection.JavaConverters._
import scala.language.higherKinds
import scala.util.{Success, Try}

case class Client(context: ClientContext = ClientContext()) {
  val logger = LoggerFactory.getLogger(getClass)

  private def createClient(): CloseableHttpClient = {
    HttpClients.custom()
      .setDefaultCookieStore(context.cookieStore)
      .setRedirectStrategy(new LaxRedirectStrategy)
      .setConnectionReuseStrategy(new DefaultConnectionReuseStrategy())
      .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
      .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
      .build()
  }

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

  class HttpDelete(uri: URI) extends HttpPost(uri) {
    override def getMethod: String = "DELETE"
  }

  private val clientLock = new ReentrantLock

  //  private val currentClient = createClient()

  def resetClient(): Unit = {
    //    clientLock.lock()
    //    try {
    //      currentClient.close()
    //      currentClient = createClient()
    //    } finally {
    //      clientLock.unlock()
    //    }
  }

  private def request(req: HttpUriRequest): Try[Response] = {
    logger.debug(s"Execute request: $req on thread ${Thread.currentThread()}")
    clientLock.lock()
    val currentClient = createClient()

    try {
      val response = currentClient.execute(req, context.httpClientContext)
      logger.debug(s"Received response: $req - $response on thread ${Thread.currentThread()}")
      try {
        Success(
          Response(
            response,
            context.httpClientContext
          )
        )
      } finally {
        response.close()
      }
    } catch {
      case e: Exception =>
        logger.error(s"Failed request $req with error on thread ${Thread.currentThread()}", e)
        throw e
    } finally {
      currentClient.close()
      clientLock.unlock()
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

  def postWithInputStream(uri: URIContainer,
                          formData: Map[String, String] = Map.empty,
                          binaries: Map[String, InputStream] = Map.empty,
                          headers: Map[String, String] = Map.empty): Try[Response] = {

    val post = new HttpPost(uri)
    val builder = MultipartEntityBuilder.create()

    formData.foreach(e => builder.addTextBody(e._1, e._2))
    binaries.foreach(e => builder.addBinaryBody(e._1, e._2))

    post.setEntity(builder.build())

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
