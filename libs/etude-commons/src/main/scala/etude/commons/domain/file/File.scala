package etude.commons.domain.file

import java.nio.file.{Path => JavaPath, Files}

case class File(javaPath: JavaPath) extends ValidPath {
  def exists: Boolean = Files.exists(javaPath)
}