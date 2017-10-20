package com.demo.search.controllers

import com.demo.search.service.SearchService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
class SearchController 
@Autowired constructor(private val searchService: SearchService) {
    companion object {
        private val logger = LoggerFactory.getLogger(SearchController::class.java)
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/search/{term}")
    fun search(@PathVariable term: String) = searchService.search(term)
}
