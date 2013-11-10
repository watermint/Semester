package stenographer.controllers

import com.twitter.finatra.Controller
import stenographer.View

/**
 *
 */
class Root extends Controller {
  val title = "Stenographer"

  val content = <h1>{title}</h1>

  get("/") {
    request =>
      render
        .html(View.skeleton(title, content))
        .toFuture
  }
}
