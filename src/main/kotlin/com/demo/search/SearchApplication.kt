package com.demo.search

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SearchApplication {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(SearchApplication::class.java, *args)
        }
    }
}
