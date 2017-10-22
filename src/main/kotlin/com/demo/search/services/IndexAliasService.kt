package com.demo.search.service

import java.net.URI
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
import com.demo.search.models.SwapAliasRequest

@Service
class IndexAliasService 
@Autowired constructor(private val restTemplate: RestTemplate) {
    companion object {
        private val logger = LoggerFactory.getLogger(IndexAliasService::class.java)
    }

    @Value("\${elasticsearch.url}")
    private lateinit var esUrl: String

    @Value("\${elasticsearch.index}")
    private lateinit var indexName: String

    @Value("\${elasticsearch.readAlias}")
    private lateinit var readAlias: String

    @Value("\${elasticsearch.writeAlias}")
    private lateinit var writeAlias: String

    private fun indexUrl() = "${esUrl}/${indexName}"

    fun establishWriteAlias() {
        val exists = aliasExists("_all", writeAlias)
        if (exists) {
            deleteAlias("_all", writeAlias)
        }
        createAlias(indexName, writeAlias)
    }
    fun establishReadAlias() {
        val exists = aliasExists("_all", readAlias)
        if (!exists) {
            createAlias(indexName, readAlias)
        }
    }

    fun swapAlias() {
        val exists = aliasExists("_all", readAlias)
        if (exists) {
            val body = SwapAliasRequest.build("_all", readAlias, indexName, readAlias)
            restTemplate.postForObject(URI("${esUrl}/_aliases"), body, String::class.java)
        } else {
            createAlias(indexName, readAlias)
        }
    }

    private fun createAlias(indexName: String, aliasName: String) {
        try {
            restTemplate.put(URI("${esUrl}/${indexName}/_alias/${aliasName}"), null)
        } catch (ex: HttpStatusCodeException) {
            logger.error("failed to create alias {} for index {}", aliasName, indexName)
            throw ex
        }
    }
    private fun deleteAlias(indexName: String, aliasName: String) {
        try {
            restTemplate.delete("${esUrl}/${indexName}/_alias/${aliasName}")
        } catch (ex: HttpStatusCodeException) {
            logger.error("failed to delete alias: {} for index {}", aliasName, indexName, ex)
            throw ex
        }
    }
    private fun aliasExists(indexName: String, aliasName: String): Boolean {
        try {
            restTemplate.headForHeaders("${esUrl}/${indexName}/_alias/${aliasName}")
            return true
        } catch (ex: HttpStatusCodeException) {
            if (ex.statusCode == HttpStatus.NOT_FOUND) {
                return false
            } else {
                throw ex
            }
        }
    }
}
