package com.yuvimasory.offpedia

import info.bliki.wiki.filter.Encoder
import info.bliki.wiki.filter.PlainTextConverter
import info.bliki.wiki.model.WikiModel

import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

//This is not written in idiomatic Scala for performance reasons
//A Wikipedia English dump is 32+ GB
object SAXHandler extends DefaultHandler {

  //--------- START DEBUG
  var tagCount = 0
  // var maxTags  = 10000000
  var maxTags  = Integer.MAX_VALUE
  //--------- END DEBUG

  var writer =
    new java.io.BufferedWriter(
      new java.io.FileWriter(
        new java.io.File(Main.outLoc)))


  var curValue: StringBuffer  = null

  var redirect: Boolean = false
  var curTitle: String  = null
  var curText: String   = null

  override def endDocument() = cleanup


  override def startElement(uri: String, lName: String, qName: String,
                            attrs: Attributes) {
    //--------- START DEBUG
    tagCount += 1
    if (tagCount > maxTags) {
      println(tagCount + " tags reached")
      cleanup()
      sys.exit(0)
    }
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
            if (keepTitle(curTitle) && keepText(curText)) {
              val transText = wiki2html(curText)
              writer write (curTitle + "\t" + transText)
              cleanup()
              sys.exit()
            }

    if (qName == "redirect")   redirect = true
    else if (qName == "title") curTitle = curValue.toString
    else if (qName == "text")  curText  = curValue.toString
  }


  private def encode(str: String) = Encoder.encodeHtml(str)


  private def wiki2text(wiki: String) = {
    val model = new WikiModel("", "")
    model.render(new PlainTextConverter(), wiki)
  }


  private def wiki2html(wiki: String) = {
    val model = new WikiModel("", "")
    model render wiki
  }


  private def keepTitle(title: String) =
    title.startsWith("Wikipedia:") == false


  private def keepText(text: String) = true


  private def cleanup() {
    writer flush()
    writer close()
  }
}
