package com.demo.search.controllers

import com.demo.search.models.Movie
import com.demo.search.services.SearchService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
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

    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/upsert")
    fun upsert(@RequestBody movie: Movie) = searchService.upsertOne(movie)

    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/upsert/bulk")
    fun bulk() = searchService.upsertMany()
    
    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/delete/{id}")
    fun del(@PathVariable id: String) = searchService.deleteOne(id)
}

