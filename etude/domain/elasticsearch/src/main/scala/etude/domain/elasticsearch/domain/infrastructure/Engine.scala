package etude.domain.elasticsearch.domain.infrastructure

import org.elasticsearch.client.Client
import java.nio.file.Path

trait Engine {
  def createClient: Client
}

object Engine {
  def ofEmbedded(clusterName: String,
                 storagePath: Path): Engine = EmbeddedEngine(clusterName, storagePath)
}