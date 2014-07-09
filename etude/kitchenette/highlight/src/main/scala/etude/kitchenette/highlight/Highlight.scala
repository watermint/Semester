package etude.kitchenette.highlight

import java.io.InputStreamReader
import javax.script.{ScriptEngine, ScriptEngineManager}

import jdk.nashorn.api.scripting.JSObject

import scala.concurrent.{Lock, ExecutionContext, Future}

class Highlight {
  private lazy val engine: ScriptEngine = {
    val n = new ScriptEngineManager().getEngineByName("nashorn")

    n.eval(new InputStreamReader(getClass.getResourceAsStream("/highlight.pack.js")))
    n
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
