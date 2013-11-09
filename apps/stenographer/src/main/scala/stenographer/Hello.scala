package stenographer

import com.twitter.finatra.{config, FinatraServer, Controller}
import java.io.File

class Hello extends Controller {

  get("/hello/:name") { request =>
    val name = request.routeParams.getOrElse("name", "anonymous")
    render.plain("hello " + name).toFuture
  }

  get("/hoge.html") { request =>

    render.static("/hoge.html").toFuture
  }

}

object App extends FinatraServer {
  System.setProperty("com.twitter.finatra.config.docRoot", "apps/stenographer/src/main/resources")
  register(new Hello())
}