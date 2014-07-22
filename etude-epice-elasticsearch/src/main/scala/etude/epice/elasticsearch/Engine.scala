package etude.epice.elasticsearch

import java.nio.file.Path

import org.elasticsearch.client.Client
import org.elasticsearch.node.Node

trait Engine {
  val client: Client
}

object Engine {
  def ofEmbedded(clusterName: String,
                 storagePath: Path): Engine = EmbeddedEngine(clusterName, storagePath)
}
