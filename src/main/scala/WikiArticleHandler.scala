package com.yuvimasory.offpedia

class WikiArticleHandler() {

  val writer =
    new java.io.BufferedWriter(
      new java.io.FileWriter(
        new java.io.File(Main.outLoc)))

  def cleanup() {
    writer flush()
    writer close()
  }

  def handle(article: WikiArticle) {
    article match {
      case WikiArticle(title, text) => {
        val transText = Bliki.wiki2html(text)
        writer write (title + "\t" + transText + "\n")
      }
    }
  }

  def keepWikiArticle(article: WikiArticle) =
    article match {
      case WikiArticle(title, text) =>
        keepWikiTitle(title) && keepWikiText(text)
    }

  private def keepWikiTitle(title: String) =
    title.startsWith("Wikipedia:") == false

  private def keepWikiText(text: String) = true
}

case class WikiArticle(title: String, text: String)
