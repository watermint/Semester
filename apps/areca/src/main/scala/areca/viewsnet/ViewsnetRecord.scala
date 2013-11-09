package areca.viewsnet

import java.time.{ZoneId, Instant}
import areca.util.Parser
import java.nio.file.Path
import scala.xml.{Node, Text}

case class ViewsnetRecord(date: Instant,
                          description: String,
                          amount: Int)

object ViewsnetRecord {
  val parser = Parser(ZoneId.of("Asia/Tokyo"))

  def fromInvoiceRow(row: Node): Option[ViewsnetRecord] = {
    val cols = row \ "td"

    if (cols.size < 1) {
      // ignore header row
      None
    } else {
      parser.date(cols(0).text.trim) match {
        case Some(date) =>
          Some(
            ViewsnetRecord(
              date = date,
              description = cols(2).text.trim,
              amount = parser.number(cols(3).text.trim).get
            )
          )
        case _ =>
          None
      }
    }
  }

  def fromInvoice(xml: Node): Seq[ViewsnetRecord] = {
    ((xml \\ "table" filter (_ \ "@class" contains Text("listtable2"))) \ "tbody" \ "tr").flatMap(fromInvoiceRow)
  }

  def fromUnfinalizedInvoiceRow(row: Node): Option[ViewsnetRecord] = {
    val cols = row \ "td"

    if (cols.size < 1) {
      // ignore header row
      None
    } else {
      parser.date(cols(0).text.trim) match {
        case Some(date) =>
          Some(
            ViewsnetRecord(
              date = date,
              description = cols(1).child(2).text.trim,
              amount = parser.number(cols(2).child(1).text.trim).get
            )
          )
        case _ =>
          None
      }
    }
  }

  def fromUnfinalizedInvoice(xml: Node): Seq[ViewsnetRecord] = {
    ((xml \\ "table" filter (_ \ "@class" contains Text("listtable5"))) \ "tbody" \ "tr").flatMap(fromUnfinalizedInvoiceRow)
  }

  def fromFile(path: Path): Seq[ViewsnetRecord] = {
    parser.html(path, "MS932") match {
      case Some(xml) =>
        fromInvoice(xml) ++ fromUnfinalizedInvoice(xml)

      case _ => Seq()
    }
  }
}