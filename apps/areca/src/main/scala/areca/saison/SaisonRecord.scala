package areca.saison

import java.nio.file.Path
import java.time.{ZoneId, Instant}
import scala.xml.{Node, Text}
import areca.util.Parser

/**
 * Netアンサー「最近のカードご利用一覧」ページのHTMLからスクレイピング.
 *
 * @param date ご利用日
 * @param description ご利用店名及び商品名
 * @param family 家族
 * @param paymentCategory 支払区分 (e.g. 1回)
 * @param revolving リボ変更
 * @param amount ご利用金額
 * @param payMonth お支払月
 * @param note 備考
 */
case class SaisonRecord(date: Instant,
                        description: String,
                        family: String,
                        paymentCategory: String,
                        revolving: String,
                        amount: Int,
                        payMonth: String,
                        note: String)

object SaisonRecord {
  val parser = Parser(ZoneId.of("Asia/Tokyo"))

  def fromAmount(text: String): Option[Int] = {
    parser.number(text.replace("円", ""))
  }

  def fromRow(row: Node): Option[SaisonRecord] = {
    val cols = row \ "td"
    if (cols.size < 1) {
      // Ignore header row
      None
    } else {
      Some(
        SaisonRecord(
          date = parser.date(cols(0).text).get,
          description = cols(1).text.trim,
          family = cols(2).text.trim,
          paymentCategory = cols(3).text.trim,
          revolving = cols(4).text.trim,
          amount = fromAmount(cols(5).text.trim).get,
          payMonth = cols(6).text.trim,
          note = cols(7).text.trim
        )
      )
    }
  }

  def fromFile(path: Path): Seq[SaisonRecord] = {
    parser.html(path, "MS932") match {
      case Some(xml) =>
        // XPath: //*[@id="main-box"]/table/tbody/tr
        ((xml \\ "div" filter (_ \ "@id" contains Text("main-box"))) \ "table" \\ "tr").flatMap(fromRow)
      case _ => Seq()
    }
  }
}