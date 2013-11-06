package etude.file

import java.nio.file.{Path => JavaPath, Files}
import scala.util.{Success, Try}

/**
 *
 */
case class Symlink(javaPath: JavaPath) extends ValidPath {
  def target: Try[Path] = Success(Path(javaPath.getParent.resolve(Files.readSymbolicLink(javaPath))))

}
