package etude.app.bolognese

import akka.actor.{Actor, ActorSystem, Props}
import akka.io.IO
import etude.foundation.logging.LoggerFactory
import spray.can.Http
import spray.http.{HttpMethods, HttpRequest, HttpResponse, Uri}

class Main extends Actor {
  val logger = LoggerFactory.getLogger(getClass)

  def receive: Receive = {
    case _: Http.Connected => sender ! Http.Register(self)

    case HttpRequest(HttpMethods.GET, Uri.Path(path), _, _, _) =>
      sender ! HttpResponse(entity = s"Pong $path")

    case e =>
      logger.info(e.toString)
  }
}

object Main {
  implicit val system = ActorSystem("Bolognese")

  val bolognese = system.actorOf(Props[Main], name = "Bolognese")

  def main(args: Array[String]): Unit = {
    IO(Http) ! Http.Bind(bolognese, interface = "127.0.0.1", port = 7000)
  }
}
