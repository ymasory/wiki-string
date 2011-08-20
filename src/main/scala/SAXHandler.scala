package com.yuvimasory.offpedia

import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

//This is not written in idiomatic Scala for performance reasons
//A Wikipedia English dump is 32+ GB
object SAXHandler extends DefaultHandler {

  var tagCount = 0
  var maxTags  = 10000000

  var redirect: Boolean = false
  var curValue: String  = null
  var curTitle: String  = null
  var curText: String   = null

  override def startElement(uri: String, lName: String, qName: String,
                            attrs: Attributes) {
    tagCount += 1
    if (tagCount > maxTags) throw new RuntimeException
    if (qName == "page") {                              
      redirect = false
      curValue = null
      curTitle = null
      curText  = null
    }
  }

  override def characters(ch: Array[Char], start: Int, len: Int) {
    curValue = new String(ch, start, len)
  }

  override def endElement(uri: String, lName: String, qName: String) {
    if (qName == "page") {
      if (redirect == false) {
        if (curTitle != null) {
          if (curText != null) {
            // println(curTitle + " of size " + curText.length)
          }
        }
      }
    }

    if (qName == "redirect")   redirect = true
    else if (qName == "title") curTitle = curValue
    else if (qName == "text")  curText  = curValue
  }
}
