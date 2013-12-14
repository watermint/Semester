package etude.chatwork.infrastructure.elasticsearch

import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.node.{Node, NodeBuilder}
import etude.commons.domain.file.Dir

case class EmbeddedEngine(storagePath: Dir) extends Engine {
  lazy val node: Node = {
    NodeBuilder
      .nodeBuilder()
      .clusterName("stenographer")
      .local(true)
      .settings(
      ImmutableSettings
        .settingsBuilder()
        .put("path.home", storagePath.javaFile)
        .put("http.enabled", false)
    ).node()
  }

  lazy val client: Client = node.client()
}
