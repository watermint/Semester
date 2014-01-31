package etude.app.gare

import scala.collection.mutable
import com.twitter.finagle.Service
import org.jboss.netty.handler.codec.http.{DefaultHttpResponse, HttpResponse, HttpRequest}
import java.net.{URI, InetSocketAddress, ConnectException, Socket}
import com.twitter.finagle.builder.{ServerBuilder, ClientBuilder}
import com.twitter.finagle.http.Http
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.HttpVersion._
import org.jboss.netty.handler.codec.http.HttpResponseStatus._

case class Dispatcher(dispatcherPort: Int,
                      portMapping: Map[String, Int]) {

  val portToClient = mutable.Map[Int, Service[HttpRequest, HttpResponse]]()

  def isPortAlive(port: Int): Boolean = {
    try {
      new Socket("localhost", port)
      true
    } catch {
      case e: ConnectException =>
        false
    }
  }

  def newClient(port: Int): Option[Service[HttpRequest, HttpResponse]] = {
    if (isPortAlive(port)) {
      Some(
        ClientBuilder()
          .codec(Http())
          .hosts(new InetSocketAddress(port))
          .hostConnectionLimit(1)
          .build()
      )
    } else {
      None
    }
  }

  def getClient(port: Int): Option[Service[HttpRequest, HttpResponse]] = {
    portToClient.get(port) match {
      case Some(client) => Some(client)
      case None =>
        newClient(port) match {
          case Some(client) =>
            portToClient.put(port, client)
            Some(client)
          case _ =>
            None
        }
    }
  }

  def defaultResponse(request: HttpRequest): Future[HttpResponse] = {
    Future.value(new DefaultHttpResponse(HTTP_1_1, NOT_FOUND))
  }

  def dispatchPort(request: HttpRequest)(port: Int): Future[HttpResponse] = {
    getClient(port) match {
      case Some(client) => client(request)
      case _ => defaultResponse(request)
    }
  }

  def dispatch(request: HttpRequest): Future[HttpResponse] = {
    getHost(request) match {
      case Some(host) =>
        portMapping.get(host) match {
          case None => defaultResponse(request)
          case Some(port) => dispatchPort(request)(port)
        }
      case _ =>
        defaultResponse(request)
    }
  }

  def getHost(request: HttpRequest): Option[String] = {
    request.headers().get("Host") match {
      case null => None
      case host =>
        try {
          val uri = new URI("http://" + host)
          Some(uri.getHost.toLowerCase)
        } catch {
          case _: Throwable => None
        }
    }
  }

  def start(): Unit = {
    val proxyService = new Service[HttpRequest, HttpResponse] {
      def apply(request: HttpRequest): Future[HttpResponse] = {
        dispatch(request)
      }
    }

    ServerBuilder()
      .codec(Http())
      .bindTo(new InetSocketAddress(dispatcherPort))
      .name("httpProxy")
      .build(proxyService)
  }
}
