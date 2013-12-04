package stenographer.views

import com.twitter.finatra.Controller
import stenographer.App

/**
 *
 */
class Root extends Controller {
  val title = "Stenographer"

  val content = <h1>{title}</h1>

  get("/") {
    request =>
      render
        .html(App.html(title, content))
        .toFuture
  }
}
