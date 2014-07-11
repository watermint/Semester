package etude.kitchenette.elasticsearch

import java.nio.file.Path

import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.node.{Node, NodeBuilder}

case class EmbeddedEngine(clusterName: String,
                          storagePath: Path) extends Engine {

  lazy val node: Node = {
    NodeBuilder
      .nodeBuilder()
      .clusterName(clusterName)
      .local(true)
      .settings(
        ImmutableSettings
          .settingsBuilder()
          .put("path.home", storagePath)
          .put("path.logs", storagePath.resolve("logs"))
          .put("http.enabled", false)
      ).node()
  }

  protected def createClient: Client = {
    node.client()
  }

  lazy val client: Client = createClient
}
