package com.demo.search

import com.jayway.restassured.RestAssured
import com.jayway.restassured.filter.log.RequestLoggingFilter
import com.jayway.restassured.filter.log.ResponseLoggingFilter
import com.jayway.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.startsWith
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import java.util.Collections

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
    }

    @After
    fun after() {
        RestAssured.replaceFiltersWith(Collections.emptyList())
    }

    @Test
    fun `should create index`() {
        RestAssured.given()
            .get("/index")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())

        RestAssured.given()
            .post("/index")
            .then()
            .statusCode(HttpStatus.CREATED.value())

        RestAssured.given()
            .get("/index")
            .then()
            .statusCode(HttpStatus.OK.value())
    }
}
