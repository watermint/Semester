package stenographer.models

import stenographer.App
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.node.{Node, NodeBuilder}
import org.elasticsearch.client.Client
import scala.collection.mutable

object Storage {
  lazy val node: Node = {
    NodeBuilder
      .nodeBuilder()
      .clusterName("stenographer")
      .local(true)
      .settings(
      ImmutableSettings
        .settingsBuilder()
        .put("path.home", App.elasticSearchHome.javaPath.toFile) // TODO refactor Dir
        .put("http.enabled", false)
    ).node()
  }

  lazy val client: Client = node.client()
}
