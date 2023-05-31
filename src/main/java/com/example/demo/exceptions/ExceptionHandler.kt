package com.example.demo.exceptions

import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException


@ControllerAdvice
class WrapperExceptionHandler {
    @ExceptionHandler(HttpClientErrorException::class)
    fun handleClientError(ex: HttpClientErrorException): ResponseEntity<ExceptionResponse> {
        // Custom error handling for client errors (e.g., 4xx)
        val status: HttpStatusCode = ex.statusCode
        // TODO: Don't send the ex.message as it is, security risk. Send a generic or modified message in production
        val errorMessage = "Client Error: " + ex.message
        return ResponseEntity(ExceptionResponse(errorMessage), status)
    }

    @ExceptionHandler(HttpServerErrorException::class)
    fun handleServerError(ex: HttpServerErrorException): ResponseEntity<ExceptionResponse> {
        // Custom error handling for server errors (e.g., 5xx)
        val status: HttpStatusCode = ex.statusCode
        // TODO: Don't send the ex.message as it is, security risk. Send a generic or modified message in production
        val errorMessage = "Server Error: " + ex.message
        return ResponseEntity(ExceptionResponse(errorMessage), status)
    }
}