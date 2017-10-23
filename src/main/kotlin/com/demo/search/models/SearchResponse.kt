package com.demo.search.models

data class SearchResponse(val results: List<SearchResult>)
data class SearchResult(val id: String, val score: Float, val title: String)
