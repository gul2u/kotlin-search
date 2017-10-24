package com.demo.search

import com.jayway.restassured.RestAssured
import com.jayway.restassured.filter.log.RequestLoggingFilter
import com.jayway.restassured.filter.log.ResponseLoggingFilter
import com.jayway.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import java.util.Collections

import com.demo.search.models.Movie
import com.demo.search.models.BulkRequest

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RelevancyTests {
    @Value("\${local.server.port}")
    var serverPort: Int? = null

    @Before
    fun before() {
        RestAssured.port = serverPort!!
        RestAssured.replaceFiltersWith(ResponseLoggingFilter.responseLogger(), RequestLoggingFilter())

        /* remove any prior index */
        RestAssured.given()
            .delete("/index")
            .then()
            .statusCode(HttpStatus.OK.value())

        RestAssured.given()
                .post("/index")
                .then()
                .statusCode(HttpStatus.CREATED.value())
    }

    @After
    fun after() {
        /* delete the index */
        RestAssured.given()
                .delete("/index")
                .then()
                .statusCode(HttpStatus.OK.value())

        RestAssured.replaceFiltersWith(Collections.emptyList())
    }

    @Test
    fun `should put exact match first`() {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .content(BulkRequest(listOf(
                Movie("2222", "The Ring"), 
                Movie("232111", "The Ring of Fire"), 
                Movie("11111", "Lord of the Rings: The Fellowship of the Ring"), 
                Movie("3389", "The Partial Matching Ring"))))
            .post("/upsert/bulk")
            .then()
            .statusCode(HttpStatus.OK.value())

        RestAssured.given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .get("/search/the ring")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("results", hasSize<Int>(4))
            .body("results[0].title", equalTo("The Ring"))
    }
}
