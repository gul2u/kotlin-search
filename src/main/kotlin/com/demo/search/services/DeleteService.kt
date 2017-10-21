package com.demo.search.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class DeleteService @Autowired constructor(private val restTemplate: RestTemplate) {
    companion object {
        private val logger = LoggerFactory.getLogger(DeleteService::class.java)
    }

    fun deleteById(id: String) {}
}
