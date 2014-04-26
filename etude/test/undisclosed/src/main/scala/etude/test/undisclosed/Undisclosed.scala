package etude.test.undisclosed

import java.io.{FileInputStream, File}
import org.specs2.execute.Result
import java.nio.file.Files
import java.util.Properties
import com.twitter.logging.Logger

case class Undisclosed(clazzName: String) {
  lazy val logger: Logger = Logger.get(getClass)

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
          logger.warning(e, s"Failed to load file: $file for test $clazzName")
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
        logger.warning(s"Skipping test: $clazzName, place file at $file")
        Result.unit()
    }
  }
}
