package com.demo.search.models

data class SearchResponse(val results: List<SearchResult>)
data class SearchResult(val id: String, val score: Float, val explain: List<String>, val title: String)
