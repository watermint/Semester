package suruga

import java.time.{ZoneId, Instant}
import util.Parser
import java.nio.file.Path
import scala.io.Source

case class SurugaRecord(date: Instant,
                        withdraw: Option[Int],
                        deposit: Option[Int],
                        description: String,
                        transactionCategory: String,
                        balance: Int,
                        note: String)

object SurugaRecord {
  val parser = Parser(ZoneId.of("Asia/Tokyo"))

  def fromLine(line: String): Option[SurugaRecord] = {
    val cols = line.split(",").map(_.replace("\"", ""))

    parser.date(cols(0)) match {
      case Some(date) =>
        Some(
          SurugaRecord(
            date = date,
            withdraw = parser.number(cols(1)),
            deposit = parser.number(cols(2)),
            description = cols(3),
            transactionCategory = cols(4),
            balance = parser.number(cols(5)).get,
            note = cols(6)
          )
        )
      case _ =>
        None
    }
  }

  def fromFile(path: Path): Seq[SurugaRecord] = {
    Source.fromFile(path.toFile, "MS932")
      .getLines()
      .toList
      .flatMap(fromLine)
  }
}