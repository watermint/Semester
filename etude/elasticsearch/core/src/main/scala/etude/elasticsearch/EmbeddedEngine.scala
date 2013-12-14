package etude.elasticsearch

import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.node.{Node, NodeBuilder}
import java.nio.file.Path

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

  lazy val client: Client = node.client()
}
