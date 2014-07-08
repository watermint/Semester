package etude.kitchenette.highlight

import java.io.InputStreamReader
import javax.script.{ScriptEngine, ScriptEngineManager}

import jdk.nashorn.api.scripting.JSObject

import scala.concurrent.{Lock, ExecutionContext, Future}

class Highlight {
  private lazy val engine: ScriptEngine = {
    val n = new ScriptEngineManager().getEngineByName("nashorn")

    n.eval(new InputStreamReader(getClass.getResourceAsStream("highlight.pack.js")))
    n
  }

  private lazy val engineLock: Lock = new Lock

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

  private def evaluate(origCode: String, jsCode: String)
                      (implicit ctx: ExecutionContext): Future[HighlightedCode] = Future {
    engineLock.acquire()
    try {
      convertResult(
        origCode,
        engine.eval(jsCode).asInstanceOf[JSObject]
      )
    } finally {
      engineLock.release()
    }
  }

  def highlight(code: String)
               (implicit ctx: ExecutionContext): Future[HighlightedCode] = {
    evaluate(code, s"hljs.highlightAuto('${escapeCode(code)}')")
  }

  def highlight(code: String, lang: String)
               (implicit ctx: ExecutionContext): Future[HighlightedCode] = {
    evaluate(code, s"hljs.highlight('${escapeCode(lang)}', '${escapeCode(code)}')")
  }
}
