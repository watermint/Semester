package semester.service.things

import javax.script.{ScriptEngine, ScriptEngineManager}

import scala.util.Try

/**
 * Things app integration.
 *
 * @see https://culturedcode.com/things/
 * @see http://downloads.culturedcode.com/things/download/ThingsAppleScriptGuide.pdf
 */
object Things {
  protected val scriptEngine: ScriptEngine = new ScriptEngineManager().getEngineByName("AppleScript")

  protected val launchThings =
    """
      |set appId to id of application "Things"
      |exists application id appId
      |
      |tell application "Things" to activate
      |
      |repeat 10 times
      |  if application "Things" is running then exit repeat
      |  delay 1
      |end repeat
      |
    """.stripMargin

  protected def executeScript(script: String): Try[Unit] = Try {
    scriptEngine.eval(launchThings + script)
  }

  protected def asStringLiteral(text: String): String = {
    "\"" + text
      .replaceAll("\"", "\\\\\"")
      .replaceAll("\n", "\" & linefeed & \"") + "\""
  }

  /**
   * issue new ToDo into inbox.
   * @param title title text.
   * @param note note.
   * @param tags tags for issue.
   * @return no specific result.
   */
  def issueToDo(title: String, note: String = "", tags: Seq[String] = Seq.empty): Try[Unit] = {
    val titleLine = "name: " + asStringLiteral(title)
    val propName = Seq(titleLine)
    val propNote = if (note.isEmpty) {
      Seq.empty
    } else {
      val noteLine = "notes: " + asStringLiteral(note)
      Seq(noteLine)
    }
    val propTags = if (tags.size < 1) {
      Seq.empty
    } else {
      val tagList = asStringLiteral(tags.mkString(","))
      val tagLine = "tag names: " + tagList
      Seq(tagLine)
    }
    val props = (propName ++ propNote ++ propTags).mkString(",")

    val script =
      s"""
        |tell application "Things"
        |  set newToDo to make new to do with properties {$props}
        |end tell
      """.stripMargin

    executeScript(script)
  }
}
