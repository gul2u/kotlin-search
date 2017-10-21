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

    }
    fun establishReadAlias() {

    }

    fun swapAlias() {
    }

    private fun createReadAlias() {}
    private fun createWriteAlias() {}
    private fun deleteReadAlias() {}
    private fun aliasExists(aliasName: String): Boolean {
        try {
            restTemplate.headForHeaders("${esUrl}/_alias/${aliasName}")
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
