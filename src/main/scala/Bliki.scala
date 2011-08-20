package com.yuvimasory.offpedia

import info.bliki.wiki.filter.Encoder
import info.bliki.wiki.filter.PlainTextConverter
import info.bliki.wiki.model.WikiModel

object Bliki {

  def encode(str: String) = Encoder.encodeHtml(str)

  def wiki2text(wiki: String) = {
    val model = new WikiModel("", "")
    model.render(new PlainTextConverter(), wiki)
  }

  def wiki2html(wiki: String) = {
    val model = new WikiModel("", "")
    model render wiki
  }
}
