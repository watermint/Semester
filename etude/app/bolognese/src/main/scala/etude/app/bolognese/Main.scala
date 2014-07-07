package etude.app.bolognese

import akka.actor.ActorSystem
import spray.http.HttpCookie
import spray.routing.SimpleRoutingApp

object Main
  extends App
  with SimpleRoutingApp
  with SecureConfiguration {

  implicit val system = ActorSystem("Bolognese")

  def hello(name: String) = {
    get {
      cookie("hello") {
        c =>
          complete {
            <h1>Hello
              {name}{c.content}
            </h1>
          }
      } ~ complete {
        "no cookie"
      }
    }
  }

  def hello2() = {
    post {
      setCookie(new HttpCookie("hello", "world")) {
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
    }
  }

}
