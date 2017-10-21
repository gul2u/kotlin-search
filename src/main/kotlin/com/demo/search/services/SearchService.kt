package com.demo.search.service

import com.demo.search.models.SearchResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class SearchService @Autowired constructor(private val restTemplate: RestTemplate) {
    companion object {
        private val logger = LoggerFactory.getLogger(SearchService::class.java)
    }

    fun search(term: String) = SearchResponse(listOf())

    fun upsertOne() {}
    fun upsertMany() {}
    fun deleteOne(id: String) {}
}
