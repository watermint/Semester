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
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import org.elasticsearch.index.query.QueryBuilders

@RunWith(classOf[JUnitRunner])
class IndexSpec extends Specification {
  val clusterName: String = "test"

  val storagePath: Path = Files.createTempDirectory("embedded-engine")

  val embeddedEngine: EmbeddedEngine = EmbeddedEngine(clusterName, storagePath)

  val awaitDuration: Duration = Duration(30, SECONDS)

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

      val id1 = "t"
      val data1 = Map("name" -> "takayuki", "mail" -> "takayuki@localhost")

      Await.result(indexType.index(data1, Some(id1)), awaitDuration).id must equalTo(id1)
      val result1 = Await.result(indexType.get(id1), awaitDuration).get
      result1.getId must equalTo(id1)
      for {
        JObject(r1) <- parse(result1.getSourceAsString)
        JField("name", JString(name)) <- r1
        JField("mail", JString(mail)) <- r1
      } {
        name must equalTo("takayuki")
        mail must equalTo("takayuki@localhost")
      }

      val id2 = "k"
      Await.result(indexType.get(id2), awaitDuration) must beNone

      Await.result(index.flush, awaitDuration) must beTrue

      val all = Await.result(indexType.all(), awaitDuration)
      all.getHits.getTotalHits must equalTo(1)

      Await.result(index.delete, awaitDuration) must beTrue
    }

    "Kuromoji and Japanese" in {
      implicit val engine = embeddedEngine

      val index = Index.withKuromoji("japanese")
      val indexType = index.indexType("ja")
      val mapping = Map(
        "message" -> Map("type" -> "string", "index" -> "analyzed", "analyzer" -> "kuromoji")
      )

      Await.result(index.create, awaitDuration) must beTrue
      Await.result(indexType.putMapping(mapping), awaitDuration) must beTrue

      Await.result(indexType.index(Map("message" -> "寿司がおいしい")), awaitDuration)

      Await.result(index.flush, awaitDuration) must beTrue

      val shouldMatch = Seq(
        "寿司",
        "おいしい"
      )
      val shouldNotMatch = Seq(
        "がお",
        "いしい"
      )

      shouldMatch.forall {
        t =>
          Await.result(indexType.search(QueryBuilders.fieldQuery("message", t)), awaitDuration).getHits.totalHits() == 1
      } must beTrue
      shouldNotMatch.forall {
        t =>
          Await.result(indexType.search(QueryBuilders.fieldQuery("message", t)), awaitDuration).getHits.totalHits() == 0
      } must beTrue
    }
  }

  {
    embeddedEngine.client.close()
    Directory.delete(storagePath)
  }
}