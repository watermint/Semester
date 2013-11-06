package shinsei

import java.time.{ZoneId, Instant}
import util.Parser
import java.nio.file.Path
import scala.io.Source

/**
 * 新生銀行 取引明細CSV.
 */
case class ShinseiRecord(date: Instant,
                         reference: String,
                         description: String,
                         withdrawal: Option[Int],
                         deposit: Option[Int],
                         balance: Int)

object ShinseiRecord {
  val parser = Parser(ZoneId.of("Asia/Tokyo"))

  def fromLine(line: String): Option[ShinseiRecord] = {
    val cols = line.split("\t")

    parser.date(cols(0)) match {
      case Some(d) =>
        Some(
          ShinseiRecord(
            date = d,
            reference = cols(1),
            description = cols(2),
            withdrawal = parser.number(cols(3)),
            deposit = parser.number(cols(4)),
            balance = parser.number(cols(5)).get
          )
        )
      case None =>
        // skip headers
        None
    }
  }

  def fromFile(path: Path): Seq[ShinseiRecord] = {
    Source.fromFile(path.toFile, "UTF-16")
      .getLines()
      .toList
      .flatMap(fromLine)
  }
}