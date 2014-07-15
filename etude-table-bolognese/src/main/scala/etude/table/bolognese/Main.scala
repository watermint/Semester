package etude.table.bolognese

import java.util.concurrent.{ExecutorService, Executors}

import akka.actor.ActorSystem
import etude.gazpacho.spray.SecureConfiguration
import spray.http.HttpCookie
import spray.routing.SimpleRoutingApp
import spray.routing.directives.CachingDirectives._

import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object Main
  extends App
  with SimpleRoutingApp
  with SecureConfiguration {

  // for processing future operation within onComplete
  val executorsPool: ExecutorService = Executors.newCachedThreadPool()

  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  implicit val system = ActorSystem("Bolognese")

  def heavyOperation(name: String): Future[String] = {
    Thread.sleep(10000)
    if (name.startsWith("a")) {
      Future.successful(s"10 seconds: $name")
    } else {
      Future.failed(new IllegalArgumentException("name not starts with 'a'"))
    }
  }

  def hello(name: String) = {
    get {
      cookie("hello") {
        c =>
          complete {
            <h1>Hello
              {name}{c.content}
            </h1>
          }
      } ~ onComplete(heavyOperation(name)) {
        case Success(s) => complete {
          s
        }
        case Failure(f) => complete {
          s"error $f"
        }
      }
    }
  }

  def hello2() = {
    post {
      setCookie(
        new HttpCookie(
          name = "hello",
          content = "world",
          secure = true,
          httpOnly = true
        )
      ) {
        complete {
          <h1>POSTing</h1>
        }
      }
    }
  }

  startServer(interface = "localhost", port = 7000) {
    path("hello" / """[a-z]{2,4}""".r) {
      a =>
        hello(a) ~ hello2()
    } ~
    pathPrefix("bootstrap") {
      getFromResourceDirectory("bootstrap")
    }
  }

}
