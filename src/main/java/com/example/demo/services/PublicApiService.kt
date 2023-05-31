package com.example.demo.services

import com.example.library.PublicApiManager
import org.springframework.stereotype.Service
import kotlin.math.min

@Service
class PublicApiService {

    fun callCategoriesListApi() {
        val categories = PublicApiManager.getCategories()
        println("Categories size: ${categories?.size ?: "Null"}")
    }

    fun callRandomEntryApi() {
        val entry = PublicApiManager.getRandomEntry()
        println("Random Entry: $entry")
    }

    fun callEntriesApi(title: String?) {
        val entries = PublicApiManager.listEntries(title = title, https = true)
        println("Entries size: ${entries?.size}")

        // Printing first 3 entries
        entries?.let {
            println("Printing first 3")
            for (i in 0..min(2, it.size - 1))
                println("${i + 1}. ${it[i]}")
        }
    }

    fun callHealthReportApi() {
        val response = PublicApiManager.getHealthReport()
        println("Public API is up: $response")
    }
}