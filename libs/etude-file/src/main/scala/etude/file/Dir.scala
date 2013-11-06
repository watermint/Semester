package etude.file

import java.nio.file.{Path => JavaPath, Files, NoSuchFileException}
import scala.util.{Success, Try}
import scala.collection.JavaConversions._

/**
 *
 */
case class Dir(javaPath: JavaPath) extends ValidPath {
  lazy val children: Try[Seq[Path]] = {
    try {
      Success(
        (for (p <- Files.newDirectoryStream(javaPath).iterator()) yield {
          p
        }).toList.map(Path(_))
      )
    } catch {
      case e: NoSuchFileException =>
        Success(Seq())
    }
  }
}
