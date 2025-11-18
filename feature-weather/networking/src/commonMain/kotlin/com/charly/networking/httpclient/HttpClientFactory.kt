package com.charly.networking.httpclient

import com.charly.networking.OpenClassForMocking
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@OpenClassForMocking
class HttpClientFactory(
    private val isDebug: Boolean,
    private val httpClientEngine: HttpClientEngine
) {

    fun createHttpClient(): HttpClient {
        return HttpClient(httpClientEngine) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = if (isDebug) LogLevel.ALL else LogLevel.NONE
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }
            install(ContentNegotiation) {
                json(
                    contentType = ContentType.Any,
                    json = Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }
}
