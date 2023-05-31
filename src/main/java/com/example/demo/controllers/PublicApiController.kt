package com.example.demo.controllers

import com.example.demo.services.PublicApiService
import com.example.library.data.PublicApiEntry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/public_api")
class PublicApiController {

    @Autowired
    lateinit var publicApiService: PublicApiService

    @GetMapping("/categories")
    fun getCategories(): List<String> {
        return publicApiService.callCategoriesListApi()
    }

    @GetMapping("/health")
    fun getHealthReport(): Map<String, Boolean> {
        val healthReportStatus = publicApiService.callHealthReportApi()
        return mapOf("status" to healthReportStatus)
    }

    @GetMapping("/random")
    fun getRandomEntries(
        title: String?,
        description: String?,
        auth: String?,
        https: Boolean?,
        cors: String?,
        category: String?
    ): PublicApiEntry? {
        return publicApiService.callRandomEntryApi(title, description, auth, https, cors, category)
    }

    @GetMapping("/entries")
    fun searchEntries(
        title: String?,
        description: String?,
        auth: String?,
        https: Boolean?,
        cors: String?,
        category: String?
    ): List<PublicApiEntry> {
        val entriesResponse = publicApiService.callEntriesApi(title, description, auth, https, cors, category)
        return entriesResponse?.entries.orEmpty()
    }
}