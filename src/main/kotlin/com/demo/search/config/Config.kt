package com.demo.search.config

import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.apache.http.client.config.RequestConfig
import org.apache.http.conn.HttpClientConnectionManager
import org.apache.http.impl.NoConnectionReuseStrategy
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.springframework.web.client.RestTemplate
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory

@Configuration
class JacksonConfig {
    @Bean
    fun objectMapperBuilder(): Jackson2ObjectMapperBuilder = 
        Jackson2ObjectMapperBuilder().let { it.modulesToInstall(KotlinModule()) }
}

@Configuration
class RestTemplateConfig {
    val connectionTimeout: Int = 1000
    val connectionRequestTimeout: Int = 1000
    val socketTimeout: Int = 1000
    val maxConnectionsPerRoute: Int = 10
    val maxConnectionsTotal: Int = 25

    @Bean
    fun restTemplateConnectionManager(): HttpClientConnectionManager {
        val connectionManager = PoolingHttpClientConnectionManager()
        connectionManager.defaultMaxPerRoute = maxConnectionsPerRoute
        connectionManager.maxTotal = maxConnectionsTotal
        return connectionManager
    }

    @Bean
    fun restTemplate(connectionManager: HttpClientConnectionManager): RestTemplate {
        val requestConfig = RequestConfig.custom()
            .setConnectTimeout(connectionTimeout)
            .setConnectionRequestTimeout(connectionRequestTimeout)
            .setSocketTimeout(socketTimeout)
            .build()

        val httpClient = HttpClientBuilder.create()
            .setDefaultRequestConfig(requestConfig)
            .setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE)
            .setConnectionManager(connectionManager)
            .build()

        return RestTemplate(HttpComponentsClientHttpRequestFactory(httpClient))
    }
}
