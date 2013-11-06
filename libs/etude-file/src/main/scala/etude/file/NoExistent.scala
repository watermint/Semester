package etude.file

import java.nio.file.{Path => JavaPath, Files}

/**
 *
 */
case class NoExistent(javaPath: JavaPath) extends InvalidPath {
  def mkdirs(): Path = {
    Path(Files.createDirectories(javaPath))
  }
}
