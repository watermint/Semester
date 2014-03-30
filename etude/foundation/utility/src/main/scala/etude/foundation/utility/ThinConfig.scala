package etude.foundation.utility

import java.io.{FileInputStream, File}
import java.util.Properties
import java.nio.file.Files

object ThinConfig {
  def ofName(name: String, pathPrefix: String = ".etude/properties"): Option[Map[String, String]] = {
    val home: File = new File(System.getProperty("user.home"), pathPrefix)
    val file: File = new File(home, name + ".properties")
    if (Files.exists(file.toPath)) {
      try {
        val p = new Properties()
        p.load(new FileInputStream(file))
        Some(Converter.propertiesToMap(p))
      } catch {
        case e: Throwable =>
          None
      }
    } else {
      None
    }
  }
}
