package etude.kitchenette.elasticsearch

import java.nio.file.Path

import org.elasticsearch.client.Client

trait Engine {
  def createClient: Client
}

object Engine {
  def ofEmbedded(clusterName: String,
                 storagePath: Path): Engine = EmbeddedEngine(clusterName, storagePath)
}
