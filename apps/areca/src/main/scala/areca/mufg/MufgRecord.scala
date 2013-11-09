package areca.mufg

import java.nio.file.Path
import java.time.{ZoneId, Instant}
import scala.io.Source
import areca.util.Parser

case class MufgRecord(date: Instant,
                      description1: String,
                      description2: String,
                      withdraw: Option[Int],
                      deposit: Option[Int],
                      balance: Int,
                      note: String,
                      fundCategory: String,
                      transactionCategory: String)

object MufgRecord {
  val parser = Parser(ZoneId.of("Asia/Tokyo"))

  def fromLine(line: String): Option[MufgRecord] = {
    val cols = line.replaceAll("(\\d*),?(\\d+),(\\d+)", "$1$2$3").split(",").map(_.replace("\"", ""))

    parser.date(cols(0)) match {
      case Some(date) =>
        Some(
          MufgRecord(
            date = date,
            description1 = cols(1),
            description2 = cols(2),
            withdraw = parser.number(cols(3)),
            deposit = parser.number(cols(4)),
            balance = parser.number(cols(5)).get,
            note = cols(6),
            fundCategory = cols(7),
            transactionCategory = cols(8)
          )
        )
      case _ =>
        None
    }
  }

  def fromFile(path: Path): Seq[MufgRecord] = {
    Source.fromFile(path.toFile, "MS932")
      .getLines()
      .toList
      .flatMap(fromLine)
  }
}
