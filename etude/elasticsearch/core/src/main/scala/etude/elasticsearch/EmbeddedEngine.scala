package etude.elasticsearch

import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.node.{Node, NodeBuilder}
import java.io.File

case class EmbeddedEngine(clusterName: String,
                          storagePath: File) extends Engine {
  lazy val node: Node = {
    NodeBuilder
      .nodeBuilder()
      .clusterName(clusterName)
      .local(true)
      .settings(
      ImmutableSettings
        .settingsBuilder()
        .put("path.home", storagePath)
        .put("http.enabled", false)
    ).node()
  }

  lazy val client: Client = node.client()
}
