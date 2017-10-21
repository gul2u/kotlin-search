package com.demo.search.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class UpsertService @Autowired constructor(private val restTemplate: RestTemplate) {
    companion object {
        private val logger = LoggerFactory.getLogger(UpsertService::class.java)
    }

    fun upsertOne() {}
    fun bulk() {}
}
