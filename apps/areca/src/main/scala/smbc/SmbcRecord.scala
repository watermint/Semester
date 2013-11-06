package smbc

import java.time.{ZoneId, Instant}
import util.Parser
import java.nio.file.Path
import scala.io.Source

/**
 * SMBCお取り引きレポート.
 */
case class SmbcRecord(date: Instant,
                      withdraw: Option[Int],
                      deposit: Option[Int],
                      description: String,
                      balance: Int)

object SmbcRecord {
  val parser = Parser(ZoneId.of("Asia/Tokyo"))

  def fromLine(line: String): Option[SmbcRecord] = {
    val cols = line.split(",")

    parser.imperialDate(cols(0)) match {
      case Some(date) =>
        Some(
          SmbcRecord(
            date = date,
            withdraw = parser.number(cols(1)),
            deposit = parser.number(cols(2)),
            description = cols(3),
            balance = parser.number(cols(4)).get
          )
        )
      case _ =>
        None
    }
  }

  def fromFile(path: Path): Seq[SmbcRecord] = {
    Source.fromFile(path.toFile, "MS932")
      .getLines()
      .toList
      .flatMap(fromLine)
  }
}