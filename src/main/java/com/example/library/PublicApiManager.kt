package com.example.library

import com.example.library.data.PublicApiCategoriesResponse
import com.example.library.data.PublicApiEntriesResponse
import com.example.library.data.PublicApiEntry
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

object PublicApiManager {

    private const val BASE_URL = "https://api.publicapis.org"

    fun getCategories(): List<String> {
        val response = callThirdPartyAPI<PublicApiCategoriesResponse>("$BASE_URL/categories")
        return response?.categories.orEmpty()
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
    ): List<PublicApiEntry>? {
        val queryParams: Map<String, Any?> = mapOf(
            "title" to title,
            "description" to description,
            "auth" to auth,
            "https" to https,
            "cors" to cors,
            "category" to category
        )
        val response = callThirdPartyAPI<PublicApiEntriesResponse>("$BASE_URL/entries", queryParams)
        return response?.entries
    }

    fun getHealthReport(): String? {
        val response = callThirdPartyAPI<String>("$BASE_URL/health")
        return response
    }

    private inline fun <reified T : Any> callThirdPartyAPI(
        url: String,
        queryParams: Map<String, Any?>? = null
    ): T? {
        println("PublicApiManager: " + "Calling API...")
        println("PublicApiManager: " + "Url = $url")

        val restTemplate = RestTemplate()
        val uriBuilder = UriComponentsBuilder.fromUriString(url)

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