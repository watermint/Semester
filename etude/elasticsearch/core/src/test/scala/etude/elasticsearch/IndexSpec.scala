package etude.elasticsearch

import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import java.nio.file.Path
import java.nio.file.Files
import scala.concurrent._
import scala.concurrent.duration._
import etude.foundation.utility.file.Directory
import org.json4s._

@RunWith(classOf[JUnitRunner])
class IndexSpec extends Specification {
  val clusterName: String = "test"

  val storagePath: Path = Files.createTempDirectory("embedded-engine")

  val embeddedEngine: Engine = EmbeddedEngine(clusterName, storagePath)

  val awaitDuration: Duration = Duration(30, SECONDS)

  {
    // Configure logger
    import org.apache.log4j.{Level, BasicConfigurator, Logger}
    BasicConfigurator.configure()
    Logger.getLogger("org.elasticsearch").setLevel(Level.WARN)
  }

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

    "Type mapping" in {
      implicit val engine = embeddedEngine

      val index = Index("mapping")
      val indexType = index.indexType("testMapping")
      val mapping = Map(
        "name" -> Map("type" -> "string"),
        "mail" -> Map("type" -> "string")
      )

      Await.result(index.create, awaitDuration) must beTrue
      Await.result(indexType.putMapping(mapping), awaitDuration) must beTrue

      import org.json4s.JsonDSL._

      val id1 = "t"
      val data1 = Map("name" -> "takayuki", "mail" -> "takayuki@localhost")

      Await.result(indexType.index(data1, Some(id1)), awaitDuration).id must equalTo(id1)
      val result1 = Await.result(indexType.get(id1), awaitDuration)
      result1.id must equalTo(id1)
      for {
        JObject(r1) <- result1.data
        JField("name", JString(name)) <- r1
        JField("mail", JString(mail)) <- r1
      } {
        name must equalTo("takayuki")
        mail must equalTo("takayuki@localhost")
      }

      Await.result(index.delete, awaitDuration) must beTrue
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