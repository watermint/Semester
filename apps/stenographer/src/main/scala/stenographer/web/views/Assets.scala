package stenographer.views

import com.twitter.finatra.Controller
import java.time.{ZoneOffset, LocalDateTime}
import java.time.format.DateTimeFormatter

class Assets extends Controller {
  val cacheMaxDays = 14

  get("/assets/:path/:file") {
    request =>

      (request.routeParams.get("path"), request.routeParams.get("file")) match {
        case (Some(p), Some(f)) =>
          render
            .static(p + "/" + f)
            .header("Cache-Control", "max-age=" + (86400 * cacheMaxDays))
            .header("Expires", DateTimeFormatter.RFC_1123_DATE_TIME.format(LocalDateTime.now().plusDays(cacheMaxDays).atOffset(ZoneOffset.ofHours(0))))
            .toFuture

        case _ => render.status(404).toFuture
      }
  }
}
