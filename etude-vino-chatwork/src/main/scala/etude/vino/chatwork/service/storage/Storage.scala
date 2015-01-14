package etude.vino.chatwork.service.storage

import java.nio.file.Paths

import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.indices.IndexMissingException
import org.elasticsearch.node.{Node, NodeBuilder}
import org.json4s.JsonAST.JValue
import org.json4s.native.JsonMethods
import org.json4s.native.JsonMethods._

object Storage {
  val status = "-dev"

  val storagePath = Paths.get(System.getProperty("user.home"), s".etude-vino-chatwork$status")

  val useTransportClient = false

  def createEmbeddedNode: Node = {
    NodeBuilder
      .nodeBuilder()
      .clusterName(s"chatwork$status")
      .local(false)
      .settings(
        ImmutableSettings
          .settingsBuilder()
          .put("path.home", storagePath)
          .put("path.logs", storagePath.resolve("logs"))
          .put("index.analysis.analyzer.default.type", "custom")
          .put("index.analysis.analyzer.default.tokenizer", "kuromoji_tokenizer")
          .put("index.number_of_replicas", 0)
          .put("index.number_of_shards", 1)
          .put("http.enabled", false)
//          .put("http.port", 9200)
//          .put("http.cors.enabled", true)
//          .put("http.cors.allow-origin", "/.*/")
      ).node()
  }

  lazy val node = createEmbeddedNode

  lazy val client: Client = node.client()

  def store(indexName: String,
            typeName: String,
            idName: String,
            source: JValue): Long = {

    val response = client.prepareIndex()
      .setIndex(indexName)
      .setType(typeName)
      .setId(idName)
      .setSource(compact(render(source)))
      .execute()
      .get()

    response.getVersion

  }

  def load(indexName: String,
           typeName: String,
           idName: String): Option[JValue] = {
    try {
      val response = client.prepareGet()
        .setIndex(indexName)
        .setType(typeName)
        .setId(idName)
        .execute()
        .get()

      if (response.isExists) {
        Some(JsonMethods.parse(response.getSourceAsString))
      } else {
        None
      }
    } catch {
      case e: Exception =>
        e.getCause match {
          case _: IndexMissingException =>
            None
        }
    }
  }

  def delete(indexName: String,
             typeName: String,
             idName: String): Long = {

    val response = client.prepareDelete()
      .setIndex(indexName)
      .setType(typeName)
      .setId(idName)
      .execute()
      .get()

    response.getVersion
  }

  def shutdown(): Unit = {
    client.close()
    node.stop()
  }
}
