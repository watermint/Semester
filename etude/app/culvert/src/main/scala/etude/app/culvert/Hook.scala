package etude.app.culvert

import com.twitter.finatra.Controller

class Hook extends Controller {
  get("/:name") {
    request =>
      val name = request.routeParams.getOrElse("name", "<no name>")
      render.body(s"hello $name").toFuture
  }
}
