package etude.commons.domain.file

import java.nio.file.{Path => JavaPath, Files, NoSuchFileException}
import scala.collection.JavaConversions._

case class Dir(javaPath: JavaPath) extends ValidPath {
  lazy val children: Either[Exception, Seq[Path]] = {
    try {
      Right(
        (for (p <- Files.newDirectoryStream(javaPath).iterator()) yield {
          p
        }).toList.map(Path(_))
      )
    } catch {
      case e: NoSuchFileException =>
        Right(Seq())

      case e: Exception =>
        Left(e)
    }
  }

  def exists: Boolean = Files.exists(javaPath)

  def mkdirs: Dir = Dir(Files.createDirectories(javaPath))

  def resolveDir(dir: String): Dir = {
    Dir(javaPath.resolve(dir))
  }

  def resolveFile(filename: String): File = {
    File(javaPath.resolve(filename))
  }
}
