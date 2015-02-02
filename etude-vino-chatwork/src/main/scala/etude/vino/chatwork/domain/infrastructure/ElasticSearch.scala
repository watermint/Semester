package etude.vino.chatwork.domain.infrastructure

import java.nio.file.{Files, Paths}
import java.util.UUID

import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.indices.IndexMissingException
import org.elasticsearch.node.{Node, NodeBuilder}
import org.json4s.JsonAST.JValue
import org.json4s.native.JsonMethods
import org.json4s.native.JsonMethods._

case class ElasticSearch(testMode: Boolean = false,
                         httpEnabled: Boolean = false) {
  val clusterName = {
    if (testMode) {
      s"chatwork-${UUID.randomUUID()}"
    } else {
      "chatwork"
    }
  }

  val storagePath = {
    if (testMode) {
      Files.createTempDirectory(clusterName)
    } else {
      Paths.get(System.getProperty("user.home"), s".etude-vino-chatwork")
    }
  }

  val useTransportClient = false

  def createEmbeddedNode: Node = {
    val baseSettings = ImmutableSettings
      .settingsBuilder()
      .put("path.home", storagePath)
      .put("path.logs", storagePath.resolve("logs"))
      .put("index.analysis.analyzer.default.type", "custom")
      .put("index.analysis.analyzer.default.tokenizer", "kuromoji_tokenizer")
      .put("index.number_of_replicas", 0)
      .put("index.number_of_shards", 1)

    val settings = if (httpEnabled) {
      baseSettings
        .put("http.enabled", true)
        .put("http.port", 9200)
        .put("http.cors.enabled", true)
        .put("http.cors.allow-origin", "/.*/")
    } else {
      baseSettings
    }

    NodeBuilder
      .nodeBuilder()
      .clusterName(clusterName)
      .local(testMode)
      .settings(settings).node()
  }

  lazy val node = createEmbeddedNode

  lazy val client: Client = {
    val c = node.client()
    c.admin()
      .cluster()
      .prepareHealth()
      .setWaitForGreenStatus()
      .execute()
      .get()
    c
  }

  def flushAndRefresh(): Unit = {
    flush()
    refresh()
  }

  def flush(): Unit = {
    client.admin()
      .indices().prepareFlush()
      .execute().get
  }

  def refresh(): Unit = {
    client.admin()
      .indices().prepareRefresh()
      .execute().get
  }

  def update(indexName: String,
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

  def get(indexName: String,
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
  }
}
