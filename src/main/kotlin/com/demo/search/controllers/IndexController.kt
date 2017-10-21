package com.demo.search.controllers

import com.demo.search.service.IndexService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
class IndexController 
@Autowired constructor(private val indexService: IndexService) {
    companion object {
        private val logger = LoggerFactory.getLogger(IndexController::class.java)
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/index")
    fun index() = indexService.index()

    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/index")
    fun create() = indexService.create()

    @RequestMapping(method = arrayOf(RequestMethod.DELETE), value = "/index")
    fun drop() = indexService.drop()
}
