package etude.pain.highlight

import java.io.{BufferedReader, InputStreamReader}
import javax.script.{ScriptEngine, ScriptEngineManager}

import jdk.nashorn.api.scripting.JSObject

import scala.concurrent.{Lock, ExecutionContext, Future}

class Highlight {
  private lazy val engine: ScriptEngine = {
    val em = new ScriptEngineManager()
    val jsEngine = em.getEngineByName("nashorn")

    if (jsEngine == null) {
      import scala.collection.JavaConverters._

      throw new IllegalStateException(
        s"""
           |Nashorn engine not found:
           |  java.version: ${System.getProperty("java.version")}
           |  java.vm.version: ${System.getProperty("java.vm.version")}
           |  java.vendor: ${System.getProperty("java.vendor")}
           |  available engine(s): ${em.getEngineFactories.asScala.map(_.getEngineName).mkString(", ")}
         """.stripMargin)
    }

    val sourceName = "/highlight.pack.js"
    val source = getClass.getResourceAsStream(sourceName)
    val sourceReader = new BufferedReader(new InputStreamReader(source))

    if (sourceReader == null) {
      throw new IllegalStateException(s"required resource [$sourceName] not found")
    }

    jsEngine.eval(sourceReader)
    jsEngine
  }

  private lazy val engineLock: Lock = new Lock

  private def evalOnEngine(js: String): JSObject = {
    engineLock.acquire()
    try {
      engine.eval(js).asInstanceOf[JSObject]
    } finally {
      engineLock.release()
    }
  }

  private def convertResult(code: String, result: JSObject): HighlightedCode = {
    HighlightedCode(
      originalCode = code,
      highlighted = result.getMember("value").asInstanceOf[String],
      detectedLanguage = result.getMember("language").asInstanceOf[String]
    )
  }

  private def escapeCode(code: String): String = {
    code
      .replaceAll("'", "\\\\'")
      .replaceAll("\r", "\\\\r")
      .replaceAll("\n", "\\\\n")
  }

  private def evaluate(origCode: String, jsCode: String): HighlightedCode = {
    convertResult(
      origCode,
      evalOnEngine(jsCode)
    )
  }

  /**
   * Highlight source code.
   * @param code source code.
   * @return highlighted source code.
   */
  def highlight(code: String): HighlightedCode = {
    evaluate(code, s"hljs.highlightAuto('${escapeCode(code)}')")
  }

  /**
   * Highlight source code.
   *
   * @param code source code.
   * @param lang language name. throws exception if the language is not supported.
   * @return highlighted source code.
   */
  def highlight(code: String, lang: String): HighlightedCode = {
    evaluate(code, s"hljs.highlight('${escapeCode(lang)}', '${escapeCode(code)}')")
  }
}
