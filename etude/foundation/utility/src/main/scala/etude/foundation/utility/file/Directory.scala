package etude.foundation.utility.file

import java.nio.file.{Files, Path}
import org.apache.commons.io.FileUtils

object Directory {
  def delete(path: Path): Unit = {
    FileUtils.deleteDirectory(path.toFile)
  }

  def exists(path: Path): Boolean = {
    Files.exists(path)
  }
}
