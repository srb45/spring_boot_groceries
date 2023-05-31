package com.example.demo.services

import com.example.library.PublicApiManager
import com.example.library.data.PublicApiEntriesResponse
import com.example.library.data.PublicApiEntry
import org.springframework.stereotype.Service
import kotlin.math.min

@Service
class PublicApiService {

    fun callHealthReportApi(): Boolean {
        val response = PublicApiManager.getHealthReport()
        println("Public API is up: $response")
        return response.alive
    }

    fun callCategoriesListApi(): List<String> {
        val categoriesResponse = PublicApiManager.getCategories()
        println("Categories size: ${categoriesResponse?.count ?: "Null"}")
        return categoriesResponse?.categories.orEmpty()
    }

    fun callRandomEntryApi(
        title: String?,
        description: String?,
        auth: String?,
        https: Boolean?,
        cors: String?,
        category: String?
    ): PublicApiEntry? {
        val entry = PublicApiManager.getRandomEntry(title, description, auth, https, cors, category)
        println("Random Entry: $entry")
        return entry
    }

    fun callEntriesApi(
        title: String?,
        description: String?,
        auth: String?,
        https: Boolean?,
        cors: String?,
        category: String?
    ): PublicApiEntriesResponse? {
        val entriesResponse = PublicApiManager.listEntries(title, description, auth, https, cors, category)
        println("Entries size: ${entriesResponse?.count}")

        // Printing first 3 entries
        entriesResponse?.entries?.let {
            println("Printing first 3")
            for (i in 0..min(2, it.size - 1))
                println("${i + 1}. ${it[i]}")
        }

        return entriesResponse
    }
}