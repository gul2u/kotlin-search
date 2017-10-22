package com.demo.search.services

import org.springframework.stereotype.Service
import mbuhot.eskotlin.query.compound.bool
import mbuhot.eskotlin.query.fulltext.match

@Service 
class QueryBuilder {
    fun exactMatch(term: String) = 
        match {
            "title.exact" to {
                query = term
                boost = 2.0f
            }
        }

    fun termMatch(term: String) = 
        match {
            "title" to {
                query = term
                operator = "and"
                boost = 1.25f
            }
        }

    fun stemmedTermMatch(term: String) = 
        match {
            "title.stemmed" to {
                query = term
                operator = "and"
                boost = 1.2f
            }
        }

    fun collapseTermMatch(term: String) = 
        match {
            "title.collapse" to {
                query = term
                boost = 1.2f
            }
        }
 
    fun prefixMatch(term: String) = 
        match {
            "title.prefix" to {
                query = term
                operator = "and"
                boost = 1.05f
            }
        }

    fun build(term: String) =  
        bool {
            should = listOf(
                exactMatch(term),
                termMatch(term),
                stemmedTermMatch(term),
                collapseTermMatch(term),
                prefixMatch(term)
            )
        }.toString()
}
