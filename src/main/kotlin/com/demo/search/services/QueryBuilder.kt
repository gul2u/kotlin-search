package com.demo.search.services

import org.springframework.stereotype.Service

@Service 
class QueryBuilder {
    fun exactMatch(term: String) = """
        {
            "match": {
                "title.exact": {
                    "query": "${term}",
                    "boost": 2.0
                }
            }
        }
        """

    fun termMatch(term: String) = """ 
        {
            "match": {
                "title": {
                    "query": "${term}",
                    "operator": "and",
                    "boost": 1.25
                }
            }
        }
        """

    fun stemmedTermMatch(term: String) = """
        {
            "match": {
                "title.stemmed": {
                    "query": "${term}",
                    "operator": "and",
                    "boost": 1.2
                }
            }
        }
        """

    fun collapseTermMatch(term: String) = """
        {
            "match": {
                "title.collapse": {
                    "query": "${term}",
                    "boost": 1.2
                }
            }
        }
        """
 
    fun prefixMatch(term: String) = """
        {
            "match": {
                "title.prefix": {
                    "query": "${term}",
                    "operator": "and",
                    "boost": 1.05
                }
            }
        }
        """

    fun build(term: String) = """
        {
            "query": {
                "bool": {
                    "should": [
                        ${exactMatch(term)},
                        ${termMatch(term)},
                        ${stemmedTermMatch(term)},
                        ${collapseTermMatch(term)},
                        ${prefixMatch(term)}
                    ]
                }
            },
            "from": 0,
            "size": 25
        }
    """
}


