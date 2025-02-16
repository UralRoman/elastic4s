package com.sksamuel.elastic4s.handlers.searches

import com.sksamuel.elastic4s.handlers.searches.queries.QueryBuilderFn
import com.sksamuel.elastic4s.json.{XContentBuilder, XContentFactory}
import com.sksamuel.elastic4s.requests.searches.HighlightField

object HighlightFieldBuilderFn {

  def apply(field: HighlightField): XContentBuilder = {

    val builder = XContentFactory.obj()

    field.boundaryChars.foreach(chars => builder.field("boundary_chars", String.valueOf(chars)))
    field.boundaryMaxScan.foreach(builder.field("boundary_max_scan", _))
    field.encoder.foreach(builder.field("encoder", _))
    field.fragmenter.foreach(builder.field("fragmenter", _))
    field.fragmentOffset.foreach(builder.field("fragment_offset", _))
    field.fragmentSize.foreach(builder.field("fragment_size", _))
    field.highlightQuery.map(QueryBuilderFn.apply).foreach { highlight =>
      builder.rawField("highlight_query", highlight)
    }
    if (field.matchedFields.nonEmpty)
      builder.array("matched_fields", field.matchedFields.toArray)
    field.highlighterType.foreach(builder.field("type", _))
    field.noMatchSize.foreach(builder.field("no_match_size", _))
    field.numOfFragments.foreach(builder.field("number_of_fragments", _))
    field.order.foreach(builder.field("order", _))
    field.phraseLimit.foreach(builder.field("phrase_limit", _))
    field.requireFieldMatch.foreach(builder.field("require_field_match", _))
    field.maxAnalyzedOffset.foreach(builder.field("max_analyzed_offset", _))
    field.tagsSchema.foreach(builder.field("tags_schema", _))
    field.boundaryScanner.foreach(builder.field("boundary_scanner", _))
    field.boundaryScannerLocale.foreach(builder.field("boundary_scanner_locale", _))

    if (field.postTags.nonEmpty || field.preTags.nonEmpty) {
      if (field.postTags.isEmpty) builder.array("post_tags", Array("</em>"))
      else builder.array("post_tags", field.postTags.toArray)

      if (field.preTags.isEmpty) builder.array("pre_tags", Array("<em>"))
      else builder.array("pre_tags", field.preTags.toArray)
    }

    field.options.foreach(options => options.foreach { case (k, v) => builder.autofield(k, v) })

    builder
  }
}
