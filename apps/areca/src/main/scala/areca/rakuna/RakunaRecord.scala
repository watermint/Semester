package areca.rakuna

import areca.Main
import java.nio.file.Path
import java.time.{ZoneId, Instant}
import scala.xml.Node
import areca.util.Parser

case class RakunaRecord(date: Instant,
                        account: String,
                        category: String,
                        subCategory: String,
                        description: String,
                        amount: Int,
                        transactionType: String)

object RakunaRecord {
  lazy val parser = Parser(ZoneId.of("Asia/Tokyo"))

  lazy val targetAccount: String = Main.config.getString("areca.rakuna.targetAccount")

  def fromRow(row: Node): Option[RakunaRecord] = {
    val cols = row \ "td"
    val account = cols(1).text.trim

    (parser.date(cols(0).text.trim), account.equals(targetAccount)) match {
      case (Some(date), true) =>
        Some(
          RakunaRecord(
            date = date,
            account = account,
            category = cols(2).text.trim,
            subCategory = cols(3).text.trim,
            description = cols(4).text.trim,
            amount = parser.number(cols(5).text.trim.replace(".00", "")).get,
            transactionType = cols(6).text.trim
          )
        )

      case _ =>
        None
    }
  }

  def fromFile(path: Path): Seq[RakunaRecord] = {
    parser.html(path, "UTF-8") match {
      case Some(xml) =>
        // XPath: /html/body/table/tbody/tr
        ((xml \\ "tbody") \ "tr").flatMap(fromRow)

      case _ =>
        Seq()
    }
  }
}