package com.example.demo.exceptions

import com.fasterxml.jackson.annotation.JsonProperty

data class ExceptionResponse(
    @JsonProperty("message")
    val message: String?
)