package com.yuvimasory.wikistring

import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

object Handler extends DefaultHandler {

  private[this] val processor = new ArticleProcessor
  private[this] lazy val log = grizzled.slf4j.Logger(getClass.getSimpleName)

  private[this] var curValue: StringBuffer  = null
  private[this] var redirect: Boolean = false
  private[this] var curTitle: String  = null
  private[this] var curText: String   = null

  override def endDocument() = processor cleanup()

  override def startElement(uri: String, lName: String, qName: String,
                            attrs: Attributes) {
    if (qName == "page") redirect = false
    if (qName == "title" || qName == "text") curValue = new StringBuffer
  }

  override def characters(ch: Array[Char], start: Int, len: Int) =
    if (curValue != null) curValue append (ch, start, len)

  override def endElement(uri: String, lName: String, qName: String) = qName match {
    case "page"     => if (redirect && curTitle != null && curText != null)
                       processor process (curTitle, curText)
    case "redirect" => redirect = true
    case "title"    => curTitle = curValue.toString
    case "text"     => curText = curValue.toString
    case s          => log warn s"unhandled case $s"
  }
}
