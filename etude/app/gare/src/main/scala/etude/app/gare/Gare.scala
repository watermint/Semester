package etude.app.gare

import java.io.File
import scala.io.Source
import org.json4s.native.JsonMethods
import org.json4s.JsonAST._
import grizzled.slf4j.Logger

class Gare(configFile: File) {
  val logger = Logger[this.type]

  def start(): Unit = {
    val json = JsonMethods.parse(Source.fromFile(configFile).getLines().mkString)
    for {
      JObject(data) <- json
      JField("listenPort", JInt(listenPort)) <- data
      JField("mapping", JObject(mapping)) <- data
    } {
      val portMapping = for {
        JField("host", JString(host)) <- mapping
        JField("port", JInt(destPort)) <- mapping
      } yield {
        host -> destPort.toInt
      }

      logger.info(s"Start dispatcher on port[$listenPort] with mapping $portMapping")
      val dispatcher = Dispatcher(listenPort.toInt, portMapping.toMap)
      dispatcher.start()
    }
  }

}

object Gare {
  def main(args: Array[String]): Unit = {
    val shp = new Gare(new File(System.getProperty("user.home"), ".etude-gare/config.json"))
    shp.start()
  }
}