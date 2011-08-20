package com.yuvimasory.offpedia

import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

//This is not written in idiomatic Scala for performance reasons
//A Wikipedia English dump is 32+ GB
object SAXHandler extends DefaultHandler {

  //--------- START DEBUG
  // var tagCount = 0
  // var maxTags  = 10000000
  // var maxTags  = Integer.MAX_VALUE
  //--------- END DEBUG

  var curValue: StringBuffer  = null

  var redirect: Boolean = false
  var curTitle: String  = null
  var curText: String   = null

  val wHandler = new WikiArticleHandler()

  override def endDocument() = wHandler.cleanup

  override def startElement(uri: String, lName: String, qName: String,
                            attrs: Attributes) {
    //--------- START DEBUG
    // tagCount += 1
    // if (tagCount > maxTags) {
    //   println(tagCount + " tags reached")
    //   wHandler.cleanup()
    //   sys.exit(0)
    // }
    //--------- END DEBUG

    if (qName == "page") redirect = false
    if (qName == "title" || qName == "text") curValue = new StringBuffer
  }

  override def characters(ch: Array[Char], start: Int, len: Int) =
    if (curValue != null) curValue append (ch, start, len)

  override def endElement(uri: String, lName: String, qName: String) {
    if (qName == "page")
      if (redirect == false)
        if (curTitle != null)
          if (curText != null)
            wHandler.handle(WikiArticle(curTitle, curText))

    if (qName == "redirect")   redirect = true
    else if (qName == "title") curTitle = curValue.toString
    else if (qName == "text")  curText  = curValue.toString
  }
}
