package etude.kitchenette.highlight

import java.util.concurrent.{ExecutorService, Executors}

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, duration}

@RunWith(classOf[JUnitRunner])
class HighlightSpec
  extends Specification {

  val timeout: Duration = Duration(10, duration.SECONDS)
  val executorsPool: ExecutorService = Executors.newCachedThreadPool()
  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  "auto detect highlight" should {
    "highlight ruby code" in {
      val code = """
                   |def name()
                   |  "world"
                   |end
                   |
                   |puts "hello, #{name}"
                 """.stripMargin

      val h = new Highlight

      val autoDetect = Await.result(h.highlight(code), timeout)

      autoDetect.detectedLanguage must equalTo("ruby")
      autoDetect.originalCode must equalTo(code)

      val specified = Await.result(h.highlight(code, "ruby"), timeout)

      specified.detectedLanguage must equalTo("ruby")
      specified.originalCode must equalTo(code)
      specified.highlighted must equalTo(autoDetect.highlighted)
    }
  }
}
