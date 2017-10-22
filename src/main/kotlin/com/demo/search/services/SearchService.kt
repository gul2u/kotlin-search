package com.demo.search.services

import com.demo.search.models.SearchResponse
import com.demo.search.models.UpsertRequest
import com.demo.search.models.Movie
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.HttpStatusCodeException

@Service
class SearchService 
@Autowired constructor(
    private val restTemplate: RestTemplate, 
    private val queryBuilder: QueryBuilder) {
    
    companion object {
        private val logger = LoggerFactory.getLogger(SearchService::class.java)
    }

    @Value("\${elasticsearch.url}")
    private lateinit var esUrl: String

    @Value("\${elasticsearch.readAlias}")
    private lateinit var readAlias: String

    @Value("\${elasticsearch.writeAlias}")
    private lateinit var writeAlias: String

    fun search(term: String): SearchResponse {
        try {
            queryBuilder.build(term)
        } catch (ex: HttpStatusCodeException) {
            logger.error("query failed for {}", term, ex)
        }
        return SearchResponse(listOf())
    }

    fun upsertOne(movie: Movie) {
        try {
            restTemplate.postForObject("${esUrl}/${writeAlias}/${movie.id}/_update", UpsertRequest(movie), String::class.java)
        } catch (ex: HttpStatusCodeException) {
            logger.error("upsert failed for id={}, {}", movie.id, ex)
        }
    }

    fun upsertMany() {}
    
    fun deleteOne(id: String) {
        try {
            restTemplate.delete("${esUrl}/${writeAlias}/${id}")
        } catch (ex: HttpStatusCodeException) {
            logger.error("delete failed for id={}, {}", id, ex)
        }
    }
}
