package etude.elasticsearch

import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import java.nio.file.Path
import java.nio.file.Files
import scala.concurrent._
import scala.concurrent.duration._
import etude.foundation.utility.file.Directory
import org.apache.log4j.{Level, BasicConfigurator, Logger}

@RunWith(classOf[JUnitRunner])
class IndexSpec extends Specification {
  val clusterName: String = "test"

  val storagePath: Path = Files.createTempDirectory("embedded-engine")

  val embeddedEngine: Engine = EmbeddedEngine(clusterName, storagePath)

  val awaitDuration: Duration = Duration(30, SECONDS)

  // Configure logger
  BasicConfigurator.configure()
  Logger.getLogger("org.elasticsearch").setLevel(Level.WARN)

  "EmbeddedEngine" should {
    "Index create/delete" in {
      implicit val engine = embeddedEngine

      val index = Index("test")

      Await.result(index.exists, awaitDuration) must beFalse
      Await.result(index.create, awaitDuration) must beTrue
      Await.result(index.exists, awaitDuration) must beTrue
      Await.result(index.delete, awaitDuration) must beTrue
      Await.result(index.exists, awaitDuration) must beFalse
    }
  }

  "clean up storage" should {
    "shutdown and remove storagePath" in {
      embeddedEngine.client.close()
      Directory.delete(storagePath)
      Directory.exists(storagePath) must beFalse
    }
  }
}