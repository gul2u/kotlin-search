package com.demo.search.models

data class RawSearchResponse(val hits: SearchHits)
data class SearchHits(val hits: Array<SearchHit>)
data class SearchHit
    (val _id: String, 
     val _index: String, 
     val _type: String, 
     val _score: Float,
     val matched_queries: List<String>,
     val _source: SearchSource)

data class SearchSource
    (val title: String, 
     val created: Long, 
     val updated: Long)
