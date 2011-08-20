package com.yuvimasory.offpedia

import org.xml.sax.helpers.XMLReaderFactory

object Main {

  val fileLoc = "/media/backup/wikipedia/enwiki-latest-pages-articles.xml"
  val parserName = "org.apache.xerces.parsers.SAXParser"

  def main(args: Array[String]) {
    System setProperty ("javax.xml.parsers.SAXParserFactory", parserName)
    val reader = XMLReaderFactory.createXMLReader
    val actualParserName = reader.getClass.getName
    assert(parserName == actualParserName,
           "using unexpected parser: " + actualParserName)
    reader setContentHandler SAXHandler
    reader parse fileLoc
  }
}
