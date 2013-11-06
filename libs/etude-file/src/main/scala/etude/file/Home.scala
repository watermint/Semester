package etude.file

import java.nio.file.FileSystems

/**
 * User home path.
 */
object Home {
  lazy val user = Path(FileSystems.getDefault.getPath(System.getProperty("user.home")))
}
