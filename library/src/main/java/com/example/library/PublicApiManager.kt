package com.example.library

import com.example.library.data.PublicApiCategoriesResponse
import com.example.library.data.PublicApiEntriesResponse
import com.example.library.data.PublicApiEntry
import com.example.library.data.PublicApiHealthResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

object PublicApiManager {

    private const val BASE_URL = "https://api.publicapis.org"

    fun getHealthReport(): PublicApiHealthResponse {
        val healthReportString = callThirdPartyAPI<String>("$BASE_URL/health")
        val objectMapper = ObjectMapper() // TODO: Use static instance
        return objectMapper.readValue(healthReportString, PublicApiHealthResponse::class.java)
    }

    fun getCategories(): PublicApiCategoriesResponse? {
        return callThirdPartyAPI<PublicApiCategoriesResponse>("$BASE_URL/categories")
    }

    fun getRandomEntry(
        title: String? = null,
        description: String? = null,
        auth: String? = null,
        https: Boolean? = null,
        cors: String? = null,
        category: String? = null
    ): PublicApiEntry? {
        val queryParams: Map<String, Any?> = mapOf(
            "title" to title,
            "description" to description,
            "auth" to auth,
            "https" to https,
            "cors" to cors,
            "category" to category
        )
        val response = callThirdPartyAPI<PublicApiEntriesResponse>("$BASE_URL/random", queryParams)
        return response?.entries.orEmpty().firstOrNull()
    }

    fun listEntries(
        title: String? = null,
        description: String? = null,
        auth: String? = null,
        https: Boolean? = null,
        cors: String? = null,
        category: String? = null
    ): PublicApiEntriesResponse? {
        val queryParams: Map<String, Any?> = mapOf(
            "title" to title,
            "description" to description,
            "auth" to auth,
            "https" to https,
            "cors" to cors,
            "category" to category
        )
        return callThirdPartyAPI<PublicApiEntriesResponse>("$BASE_URL/entries", queryParams)
    }

    private inline fun <reified T : Any> callThirdPartyAPI(
        url: String,
        queryParams: Map<String, Any?>? = null
    ): T? {
        println("PublicApiManager: " + "Calling API...")
        println("PublicApiManager: " + "Url = $url")

        var builder = RestTemplateBuilder()
        builder = builder.rootUri(BASE_URL)
        builder = builder.defaultHeader("Accept", "application/json")
        // TODO: Add timeout
        // TODO: Add retry
        // TODO: Add logging
        // TODO: Add error handling
        // TODO: Add caching
        // TODO: No need to create new instance every time
        // TODO: Explore other methods of builder
        val restTemplate = RestTemplate()

        val uriBuilder = UriComponentsBuilder.fromUriString(url)

        // Removing null values from query params, adding to URI after converting to MultiValueMap
        queryParams?.let {
            val nonNullParams = it.filterValues { value -> value != null }
            val modifiedParams = nonNullParams.mapValues { entry -> listOf(entry.value.toString()) }
            val multiValueMap = LinkedMultiValueMap(modifiedParams)
            println("PublicApiManager: " + "Query = $multiValueMap")
            uriBuilder.queryParams(multiValueMap)
        }

        return restTemplate.getForObject(uriBuilder.build().toUriString(), T::class.java)
    }
}