package etude.test.undisclosed

import java.io.{FileInputStream, File}
import org.specs2.execute.Result
import java.nio.file.Files
import org.slf4j.{Logger, LoggerFactory}
import java.util.Properties

case class Undisclosed(clazzName: String) {
  lazy val logger: Logger = LoggerFactory.getLogger(getClass)

  val pathPrefix: String = ".etude-test/undisclosed"

  val home: File = new File(System.getProperty("user.home"), pathPrefix)

  val file: File = new File(home, clazzName + ".properties")

  protected def context: Option[Properties] = {
    if (Files.exists(file.toPath)) {
      try {
        val p = new Properties()
        p.load(new FileInputStream(file))
        Some(p)
      } catch {
        case e: Throwable =>
          logger.warn(s"Failed to load file: $file for test $clazzName", e)
          None
      }
    } else {
      None
    }
  }

  def undisclosed(f: Properties => Result): Result = {
    context match {
      case Some(c) =>
        logger.info(s"Testing: $clazzName")
        f(c)
      case _ =>
        logger.warn(s"Skipping test: $clazzName, place file at $file")
        Result.unit()
    }
  }
}
