package etude.pain.highlight

import java.util.concurrent.{ExecutorService, Executors}
import javax.script.ScriptException

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, duration}

@RunWith(classOf[JUnitRunner])
class HighlightSpec
  extends Specification {

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

      val autoDetect = h.highlight(code)

      autoDetect.detectedLanguage must equalTo("ruby")
      autoDetect.originalCode must equalTo(code)

      h.highlight(code, "invalid") should throwA[ScriptException]

      val specified = h.highlight(code, "ruby")

      specified.detectedLanguage must equalTo("ruby")
      specified.originalCode must equalTo(code)
      specified.highlighted must equalTo(autoDetect.highlighted)


    }
  }
}
