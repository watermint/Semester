package starbucks

import java.time.{ZoneId, Instant}
import java.nio.file.Path
import scala.xml.{Text, Node}

import util.Parser

/**
 * My STARBUCKS「残高照会・利用履歴」
 */
case class StarbucksRecord(date: Instant,
                           description: String,
                           receive: Int,
                           payment: Int)

object StarbucksRecord {
  val parser = Parser(ZoneId.of("Asia/Tokyo"))

  def fromRow(row: Node): Option[StarbucksRecord] = {
    val cols = row \ "td"
    Some(
      StarbucksRecord(
        date = parser.date(cols(0).text.trim).get,
        description = cols(1).text.trim,
        receive = parser.number(cols(2).text).get,
        payment = parser.number(cols(3).text).get
      )
    )
  }

  def fromFile(path: Path): Seq[StarbucksRecord] = {
    parser.html(path, "UTF-8") match {
      case Some(xml) =>
        // XPath: //*[@id="zandaka"]
        ((xml \\ "table" filter (_ \ "@id" contains Text("zandaka"))) \ "tbody" \ "tr").flatMap(tr => fromRow(tr))
      case _ => Seq()
    }
  }

}