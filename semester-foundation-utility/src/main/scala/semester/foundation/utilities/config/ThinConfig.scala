package semester.foundation.utilities.config

import java.io.{File, FileInputStream}
import java.nio.file.Files
import java.util.Properties

import semester.foundation.utilities.helpers.PropertiesHelper

object ThinConfig {
  def ofName(name: String, pathPrefix: String = ".etude/properties"): Option[Map[String, String]] = {
    val home: File = new File(System.getProperty("user.home"), pathPrefix)
    val file: File = new File(home, name + ".properties")
    if (Files.exists(file.toPath)) {
      try {
        val p = new Properties()
        p.load(new FileInputStream(file))
        Some(PropertiesHelper.propertiesToMap(p))
      } catch {
        case e: Throwable =>
          None
      }
    } else {
      None
    }
  }
}
