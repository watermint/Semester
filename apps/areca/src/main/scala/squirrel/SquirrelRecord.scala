package squirrel

import java.io.File
import java.time.{ZoneId, Instant}
import org.watermint.timesugar.TimeSugar
import scalax.io.{Codec, Resource}
import org.slf4j.{LoggerFactory, Logger}

/**
 * Squirrelへのインポート用.
 *
 * @param date 日付
 * @param description 明細
 * @param category カテゴリ
 * @param value 金額
 * @param note メモ
 */
case class SquirrelRecord(date: Instant,
                          description: String,
                          category: SquirrelCategory,
                          value: Long,
                          note: String) {
  lazy val timeZone = ZoneId.of("Asia/Tokyo")

  lazy val formattedDate = TimeSugar.format(date, "yyyy/MM/dd", timeZone)

  lazy val line = formattedDate +
    "," + description +
    "," + value +
    "," + note +
    "," + category.name
}

object SquirrelRecord {
  val logger = LoggerFactory.getLogger(SquirrelRecord.getClass)

  def export(output: String, records: Seq[SquirrelRecord]) = {
    records.foreach { r => logger.debug(r.toString) }

    Resource.fromFile(new File(output)).write(
      records.map(_.line).mkString("\n") + "\n"
    )(Codec.UTF8)
  }
}