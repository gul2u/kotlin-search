package com.demo.search.controllers

import com.demo.search.service.IndexService
import com.demo.search.service.IndexAliasService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
class IndexController 
@Autowired constructor(private val indexService: IndexService, private val indexAliasService: IndexAliasService) {
    companion object {
        private val logger = LoggerFactory.getLogger(IndexController::class.java)
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/index", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun index() = indexService.index() //response(indexService.index())

    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/index", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun create() : String {
        indexService.create()
        return indexService.index()
    }

    @RequestMapping(method = arrayOf(RequestMethod.DELETE), value = "/index")
    fun drop() = indexService.drop()

    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/alias/swap")
    fun swapAlias() = indexAliasService.swapAlias() 
}
