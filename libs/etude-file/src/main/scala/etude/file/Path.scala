package etude.file

import java.nio.file.{Path => JavaPath, LinkOption, Files}

/**
 * Wrapper for java.nio.file.path
 */
trait Path {
  val javaPath: JavaPath
}

object Path {
  def apply(javaPath: JavaPath): Path = {
    (
      Files.isSymbolicLink(javaPath),
      Files.isDirectory(javaPath),
      Files.exists(javaPath, LinkOption.NOFOLLOW_LINKS)
      ) match {
        case (true, _, _) => Symlink(javaPath)
      case (_, true, _) => Dir(javaPath)
      case (_, false, true) => File(javaPath)
      case _ => NoExistent(javaPath)
    }
  }
}