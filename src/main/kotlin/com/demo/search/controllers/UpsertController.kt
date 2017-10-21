package com.demo.search.controllers

import com.demo.search.service.UpsertService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
class UpsertController 
@Autowired constructor(private val upsertService: UpsertService) {
    companion object {
        private val logger = LoggerFactory.getLogger(UpsertController::class.java)
    }

    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/upsert")
    fun upsert() {}

    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/upsert/bulk")
    fun bulk() {}
}
