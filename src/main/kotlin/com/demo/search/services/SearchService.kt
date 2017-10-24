package com.demo.search.services

import java.net.URI
import com.demo.search.models.SearchResponse
import com.demo.search.models.SearchResult
import com.demo.search.models.RawSearchResponse
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

    @Value("\${elasticsearch.type}")
    private lateinit var indexType: String

    @Value("\${elasticsearch.refresh}")
    private var refresh: Boolean = false

    fun search(term: String): SearchResponse {
        try {
            val res = restTemplate.postForObject(URI("${esUrl}/${readAlias}/${indexType}/_search"), 
                queryBuilder.build(term), RawSearchResponse::class.java)
            return SearchResponse(res.hits.hits.map { SearchResult(it._id, it._score, it._source.title) }) 
        } catch (ex: HttpStatusCodeException) {
            logger.error("query failed for {}, body: {}", term, ex.getResponseBodyAsString())
            throw ex
        }
    }

    fun upsertOne(movie: Movie) {
        try {
            restTemplate.postForObject("${esUrl}/${writeAlias}/${indexType}/${movie.id}/_update?refresh=${refresh}", UpsertRequest(movie), String::class.java)
        } catch (ex: HttpStatusCodeException) {
            logger.error("upsert failed for id={}, body={}", movie.id, ex.getResponseBodyAsString())
            throw ex
        }
    }

    fun upsertMany() {}
    
    fun deleteOne(id: String) {
        try {
            restTemplate.delete("${esUrl}/${writeAlias}/${indexType}/${id}?refresh=${refresh}")
        } catch (ex: HttpStatusCodeException) {
            logger.error("delete failed for id={}, body={}", id, ex.getResponseBodyAsString())
            throw ex
        }
    }
}
