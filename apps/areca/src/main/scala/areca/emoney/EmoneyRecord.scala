package areca.emoney

import java.nio.file.Path
import java.time.{ZoneId, Instant}
import scala.io.{Codec, Source}
import areca.util.Parser

/**
 * CSV Record format for Android EMoneyReader.
 *
 * @see https://play.google.com/store/apps/details?id=com.kaemashita.android.emoneyreader
 *
 * @param seq no
 * @param date date
 * @param note description
 * @param value value
 * @param balance balance
 */
case class EmoneyRecord(seq: Int,
                        date: Instant,
                        description: String,
                        note: String,
                        value: Int,
                        balance: Int) {

}

object EmoneyRecord {
  val parser = Parser(ZoneId.of("Asia/Tokyo"))

  def fromLine(line: String): Option[EmoneyRecord] = {
    val values = line.split(",")

    (
      parser.number(values(0)),
      parser.date(values(1)),
      values(2),
      values(3),
      parser.number(values(4)),
      parser.number(values(5))
      ) match {
      case (Some(seq: Int), Some(date), description: String, note: String, Some(value), Some(balance)) =>
        Some(
          EmoneyRecord(
            seq = seq,
            date = date,
            description = description,
            note = note,
            value = value,
            balance = balance
          )
        )
      case _ => None
    }
  }

  def fromFile(path: Path): Seq[EmoneyRecord] = {
    Source.fromFile(path.toFile)(Codec.UTF8)
      .getLines()
      .toList
      .flatMap(fromLine)
  }
}