package etude.epice.html

import java.io.StringReader

import nu.validator.htmlparser.common.XmlViolationPolicy
import nu.validator.htmlparser.sax.HtmlParser

import scala.util.{Success, Try}
import scala.xml.parsing.NoBindingFactoryAdapter
import scala.xml.{InputSource, Node}

/**
 * Normalize HTML as XML.
 */
object Normalizer {
  def html(text: String): Try[Node] = {
    // reference:
    // http://www.mwsoft.jp/programming/scala/web_scraping.html
    val htmlParser = new HtmlParser
    val contentHandler = new NoBindingFactoryAdapter

    htmlParser.setNamePolicy(XmlViolationPolicy.ALLOW)
    htmlParser.setContentHandler(contentHandler)
    htmlParser.parse(new InputSource(new StringReader(text)))

    Success(contentHandler.rootElem)
  }
}
