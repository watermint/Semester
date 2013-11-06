package etude.file

import java.nio.file.{Path => JavaPath, Files}

/**
 *
 */
trait ValidPath extends Path {

  def resolve(other: String): Path = Path(javaPath.resolve(other).normalize())

  def resolve(other: JavaPath): Path = Path(javaPath.resolve(other).normalize())

  def resolve(other: Path): Path = Path(javaPath.resolve(other.javaPath).normalize())

  def isSame(other: JavaPath): Boolean = Files.isSameFile(javaPath, other)

  def isSame(other: Path): Boolean = Files.isSameFile(javaPath, other.javaPath)

  def size: Long = Files.size(javaPath)

  lazy val fileName: String = javaPath.getFileName.toString

  lazy val extName: String = {
    val lastDotPosition = fileName.lastIndexOf(".")

    if (lastDotPosition > 0 && lastDotPosition + 1 < fileName.length) {
      fileName.substring(lastDotPosition + 1)
    } else {
      ""
    }
  }

}
