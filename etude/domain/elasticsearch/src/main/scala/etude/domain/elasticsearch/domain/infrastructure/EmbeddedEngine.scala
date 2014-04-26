package etude.domain.elasticsearch.domain.infrastructure

import java.nio.file.Path
import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.node.{Node, NodeBuilder}
import org.apache.log4j.{BasicConfigurator, Logger => Log4JLogger, Level => Log4JLevel}
import com.twitter.logging.{Logger, Level}

case class EmbeddedEngine(clusterName: String,
                           storagePath: Path) extends Engine {

  val logger = Logger.get(getClass)

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
    updateLogLevel(Level.WARNING)
    node.client()
  }

  lazy val client: Client = createClient

  def log4jLevel(level: Level): Log4JLevel = {
    level match {
      case Level.ALL => Log4JLevel.ALL
      case Level.FATAL => Log4JLevel.FATAL
      case Level.CRITICAL => Log4JLevel.FATAL
      case Level.ERROR => Log4JLevel.ERROR
      case Level.WARNING => Log4JLevel.WARN
      case Level.INFO => Log4JLevel.INFO
      case Level.DEBUG => Log4JLevel.DEBUG
      case Level.TRACE => Log4JLevel.TRACE
      case Level.OFF => Log4JLevel.OFF
      case _ => throw new IllegalArgumentException(s"Invalid log level $level")
    }
  }

  def updateLogLevel(level: Level): Unit = {
    BasicConfigurator.configure()
    Log4JLogger.getLogger("org.elasticsearch").setLevel(log4jLevel(level))
  }
}
