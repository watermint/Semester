package etude.elasticsearch

import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.node.{Node, NodeBuilder}
import java.nio.file.Path
import org.apache.log4j.{Level, BasicConfigurator, Logger}

case class EmbeddedEngine(clusterName: String,
                          storagePath: Path) extends Engine {
  protected lazy val node: Node = {
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
    updateLogLevel(Level.WARN)
    node.client()
  }

  lazy val client: Client = createClient
  
  def updateLogLevel(level: Level): Unit = {
    BasicConfigurator.configure()
    Logger.getLogger("org.elasticsearch").setLevel(level)
  }
}
