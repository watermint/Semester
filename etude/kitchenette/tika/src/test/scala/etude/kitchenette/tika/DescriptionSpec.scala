package etude.kitchenette.tika

import java.nio.file.Paths
import java.util.concurrent.{Executors, ExecutorService}

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

import scala.concurrent.{Await, ExecutionContext, duration}
import scala.concurrent.duration.Duration

/**
 *
 */
@RunWith(classOf[JUnitRunner])
class DescriptionSpec
  extends Specification {

  val timeout: Duration = Duration(10, duration.SECONDS)
  val executorsPool: ExecutorService = Executors.newCachedThreadPool()
  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  "Description" should {
    "detect file type" in {
      val d = Await.result(Description.ofPath(Paths.get("build.sbt")), timeout)

      d.contentType must equalTo("text/plain; charset=ISO-8859-1")
      d.contentEncoding must equalTo("ISO-8859-1")
      d.isText must beTrue
      d.isImage must beFalse

    }
  }
}
