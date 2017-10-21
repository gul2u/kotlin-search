package com.demo.search.controllers

import com.demo.search.service.DeleteService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
class DeleteController 
@Autowired constructor(private val deleteService: DeleteService) {
    companion object {
        private val logger = LoggerFactory.getLogger(DeleteController::class.java)
    }

    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/delete/{id}")
    fun del(@PathVariable id: String) = deleteService.deleteById(id)
}
