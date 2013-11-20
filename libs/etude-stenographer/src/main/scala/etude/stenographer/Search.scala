package etude.stenographer

import etude.file.Dir
import org.elasticsearch.node.{NodeBuilder, Node}
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.client.Client

case class Search(home: Dir) {
  lazy val node: Node = {
    NodeBuilder
      .nodeBuilder()
      .clusterName("stenographer")
      .local(true)
      .settings(
      ImmutableSettings
        .settingsBuilder()
        .put("path.home", home.javaFile)
        .put("http.enabled", false)
    ).node()
  }

  lazy val client: Client = node.client()
}
