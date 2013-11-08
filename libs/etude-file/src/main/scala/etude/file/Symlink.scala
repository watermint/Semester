package etude.file

import java.nio.file.{Path => JavaPath, Files}

/**
 *
 */
case class Symlink(javaPath: JavaPath) extends ValidPath {
  def target: Either[Exception, Path] = {
    try {
      Right(Path(javaPath.getParent.resolve(Files.readSymbolicLink(javaPath))))
    } catch {
      case e: Exception => Left(e)
    }
  }

}
