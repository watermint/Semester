package etude.app.gare

import java.net.InetSocketAddress
import com.twitter.finagle.builder.{Server, ServerBuilder, ClientBuilder}
import com.twitter.finagle.Service
import com.twitter.finagle.http.Http
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpResponse}

object SimpleHttpProxy {
  def main(args: Array[String]): Unit = {
    val client: Service[HttpRequest, HttpResponse] = ClientBuilder()
      .codec(Http())
      .hosts(new InetSocketAddress(7070))
      .hostConnectionLimit(1)
      .build()

    val proxyService = new Service[HttpRequest, HttpResponse] {
      def apply(request: HttpRequest) = client(request)
    }

    val server: Server = ServerBuilder()
      .codec(Http())
      .bindTo(new InetSocketAddress(80))
      .name("httpProxy")
      .build(proxyService)
  }
}
