package etude.vino.chatwork.storage

import java.nio.file.Paths

import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.node.{Node, NodeBuilder}
import org.json4s.JsonAST.JValue
import org.json4s.native.JsonMethods._

object Storage {
  val storagePath = Paths.get(System.getProperty("user.home"), ".etude-vino-chatwork")

  val useTransportClient = false

  def createEmbeddedNode: Node = {
    NodeBuilder
      .nodeBuilder()
      .clusterName("chatwork")
      .local(false)
      .settings(
        ImmutableSettings
          .settingsBuilder()
          .put("path.home", storagePath)
          .put("path.logs", storagePath.resolve("logs"))
          .put("index.analysis.analyzer.default.type", "custom")
          .put("index.analysis.analyzer.default.tokenizer", "kuromoji_tokenizer")
          .put("http.enabled", true)
          .put("http.port", 9200)
          .put("http.cors.enabled", true)
          .put("http.cors.allow-origin", "/.*/")
      ).node()
  }

  lazy val client: Client = createEmbeddedNode.client()

  def store(indexName: String,
            typeName: String,
            idName: String,
            source: JValue): Long = {

    val response = Storage.client.prepareIndex()
      .setIndex(indexName)
      .setType(typeName)
      .setId(idName)
      .setSource(compact(render(source)))
      .execute()
      .get()

    response.getVersion
  }
}
