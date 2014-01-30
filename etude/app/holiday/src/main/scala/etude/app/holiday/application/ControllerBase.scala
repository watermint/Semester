package etude.app.holiday.application

import com.twitter.finatra.Controller
import java.util.concurrent.{Executors, ExecutorService}
import scala.concurrent.ExecutionContext
import etude.foundation.domain.lifecycle.async.AsyncEntityIOContext
import scala.concurrent.duration._

trait ControllerBase
  extends Controller {

  val executorsPool: ExecutorService = Executors.newCachedThreadPool()

  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  implicit val entityIOContext = AsyncEntityIOContext()

  val timeout = Duration(10, SECONDS)
}
