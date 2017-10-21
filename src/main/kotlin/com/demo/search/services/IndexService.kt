package com.demo.search.service

import java.net.URI
import com.demo.search.models.SearchResponse
import org.slf4j.LoggerFactory
import org.springframework.core.io.ResourceLoader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.HttpStatusCodeException

@Service
class IndexService 
@Autowired constructor(private val restTemplate: RestTemplate) {
    companion object {
        private val logger = LoggerFactory.getLogger(IndexService::class.java)
    }

    @Autowired
    private lateinit var resourceLoader: ResourceLoader

    @Value("\${elasticsearch.url}")
    private lateinit var esUrl: String

    @Value("\${elasticsearch.index}")
    private lateinit var indexName: String

    @Value("\${elasticsearch.readAlias}")
    private lateinit var readAlias: String

    @Value("\${elasticsearch.writeAlias}")
    private lateinit var writeAlias: String

    private fun indexUrl() = "${esUrl}/${indexName}"

    fun index() = restTemplate.getForObject(indexUrl(), String::class.java)
    
    fun create() {
        if (!indexExists()) {
            // create it
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON
            val req = HttpEntity<String>(indexJson(), headers)
            restTemplate.exchange(indexUrl(), HttpMethod.PUT, req, String::class.java) 

            establishWriteAlias()
            establishReadAlias()
        }
    }
    
    fun drop() {
        try {
            restTemplate.delete(URI(indexUrl()))
        } catch (ex: HttpStatusCodeException) {
            if (ex.statusCode == HttpStatus.NOT_FOUND) {
                logger.warn("tried to delete index that doesnt exist")
            } else {
                throw ex
            }
        }
    }

    private fun indexJson() = 
        resourceLoader.getResource("classpath:schema.json").inputStream.bufferedReader().use { it.readText() }
    
    private fun establishWriteAlias() {

    }
    private fun establishReadAlias() {

    }
    private fun indexExists() : Boolean {
        try {
            restTemplate.headForHeaders(URI(indexUrl()))
        } catch (ex: HttpStatusCodeException) {
            return false
        }
        return true
    }
}
