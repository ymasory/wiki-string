package com.yuvimasory.wikistring

import org.xml.sax.helpers.XMLReaderFactory

object Main {

  private[this] val Parser = "org.apache.xerces.parsers.SAXParser"

  def vmain(args: Vector[String]) {
    if (args.length != 2) {
      Console.err println "usage: Main [enwiki-XXX-pages-articles.xml] [output]"
      Console.err println()
      sys exit 1
    }
    val Vector(in, out) = args
    System setProperty ("javax.xml.parsers.SAXParserFactory", Parser)
    val reader = XMLReaderFactory.createXMLReader
    val actualParser = reader.getClass.getName
    assert(
      Parser == actualParser,
      s"using unexpected parser: $actualParser"
    )
    reader setContentHandler Handler
    reader parse in
  }

  def main(args: Array[String]) = vmain(Vector.empty ++ args)
}
