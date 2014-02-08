package etude.foundation.html

import net.htmlparser.jericho.{Element, Source, Segment}
import scala.collection.JavaConversions._
import org.jaxen.{Context, Navigator, BaseXPath}
import org.jaxen.dom.DocumentNavigator
import org.jaxen.util.SingletonList

/**
 * @see https://github.com/chimerast/scala-html-scraper
 */
case class Scraper(segment: Segment) {
  def eval(xpath: String): List[AnyRef] = {
    ScraperXPath(xpath).evaluate(segment) match {
      case list: java.util.List[_] =>
        list.toList.asInstanceOf[List[AnyRef]]
      case elem =>
        List(elem)
    }
  }
}

object Scraper {
  def apply(url: String, args: Any*): Segment = {
    DocumentNavigator
      .getInstance()
      .getDocument(url.format(args: _*))
      .asInstanceOf[Source]
  }

  def parse(text: CharSequence): Segment = {
    val doc = new Source(text)
    doc.fullSequentialParse()
    doc
  }

  implicit def segmentWrapper(segment: Segment): Scraper = Scraper(segment)
}

private
case class ScraperXPath(xpath: String,
                   navigator: Navigator = DocumentNavigator.getInstance())
  extends BaseXPath(xpath, navigator) {

  override def getContext(node: scala.Any): Context = {
    node match {
      case n: Context => n
      case _ =>
        val fullContext = new Context(getContextSupport)
        node match {
          case n: Source =>
            val rootNode: Element = getNavigator.getDocumentNode(n).asInstanceOf[Element]
            fullContext.setNodeSet(new SingletonList(rootNode))
          case n: List[_] =>
            fullContext.setNodeSet(node.asInstanceOf[List[AnyRef]])
          case _ =>
            fullContext.setNodeSet(new SingletonList(node))
        }
        fullContext
    }
  }
}