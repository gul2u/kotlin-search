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

    fun search(term: String): SearchResponse {
        try {
            val query = """
                {
                    "query": ${queryBuilder.build(term)}
                    "from": 0,
                    "size": 25
                }
            """
            val res = restTemplate.postForObject(URI("${esUrl}/${readAlias}/${indexType}/_search"), 
                query, RawSearchResponse::class.java)
            return SearchResponse(res.hits.hits.map { SearchResult(it._id, it._score, it._source.title) }) 
        } catch (ex: HttpStatusCodeException) {
            logger.error("query failed for {}", term, ex)
            throw ex
        }
    }

    fun upsertOne(movie: Movie) {
        try {
            restTemplate.postForObject("${esUrl}/${writeAlias}/${movie.id}/_update", UpsertRequest(movie), String::class.java)
        } catch (ex: HttpStatusCodeException) {
            logger.error("upsert failed for id={}, {}", movie.id, ex)
            throw ex
        }
    }

    fun upsertMany() {}
    
    fun deleteOne(id: String) {
        try {
            restTemplate.delete("${esUrl}/${writeAlias}/${indexType}/${id}")
        } catch (ex: HttpStatusCodeException) {
            logger.error("delete failed for id={}, {}", id, ex)
            throw ex
        }
    }
}
