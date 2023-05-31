package com.example.library.data

import com.fasterxml.jackson.annotation.JsonProperty

data class PublicApiCategoriesResponse(
    @JsonProperty("count")
    val count: Int,
    @JsonProperty("categories")
    val categories: List<String>
)

data class PublicApiEntriesResponse(
    @JsonProperty("count")
    val count: Int,
    @JsonProperty("entries")
    val entries: List<PublicApiEntry>
)

// TODO: Create a Converter to map text/plain with this
data class PublicApiHealthResponse(
    @JsonProperty("alive")
    val alive: Boolean = false
)

data class PublicApiEntry(
    @JsonProperty("API")
    val api: String,
    @JsonProperty("Description")
    val description: String,
    @JsonProperty("Auth")
    val auth: String,
    @JsonProperty("HTTPS")
    val https: Boolean,
    @JsonProperty("Cors")
    val cors: String,
    @JsonProperty("Link")
    val link: String,
    @JsonProperty("Category")
    val category: String,
)