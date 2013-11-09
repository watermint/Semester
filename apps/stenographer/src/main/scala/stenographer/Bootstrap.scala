package stenographer

import com.twitter.finatra.Controller

class Bootstrap extends Controller {
  get("/assets/:path/:file") {
    request =>
      (request.routeParams.get("path"), request.routeParams.get("file")) match {
        case (Some(p), Some(f)) =>
          render.static(p + "/" + f).toFuture
        case _ => render.status(404).toFuture
      }
  }
}
