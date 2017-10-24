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
class SearchTests {
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
    fun `should get the index`() {
        RestAssured.given()
            .get("/index")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun `should insert document`() {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .content("""{"id": "12345", "title": "Gravity"}""")
            .post("/upsert")
            .then()
            .statusCode(HttpStatus.OK.value())
    }
    
    @Test
    fun `should bulk insert documents`() {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .content(BulkRequest(listOf(Movie("2222", "Ring"), Movie("11111", "Ring"), Movie("3389", "Ring"))))
            .post("/upsert/bulk")
            .then()
            .statusCode(HttpStatus.OK.value())

        RestAssured.given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .get("/search/ring")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("results", hasSize<Int>(3))
    }

    @Test
    fun `should find movies by title`() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .content("""{"id": "12345", "title": "Gravity"}""")
                .post("/upsert")
                .then()
                .statusCode(HttpStatus.OK.value())

        RestAssured.given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .get("/search/gravity")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("results", hasSize<Int>(1))
            .body("results[0].title", equalTo("Gravity"))
    }

    @Test
    fun `should delete document`() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .content("""{"id": "12345", "title": "Gravity"}""")
                .post("/upsert")
                .then()
                .statusCode(HttpStatus.OK.value())

        RestAssured.given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .post("/delete/12345")
            .then()
            .statusCode(HttpStatus.OK.value())

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .get("/search/gravity")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("results", hasSize<Int>(0))
    }
}
