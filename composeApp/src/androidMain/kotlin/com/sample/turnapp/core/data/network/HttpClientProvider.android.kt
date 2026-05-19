package com.sample.turnapp.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient

actual fun createHttpClient(): HttpClient {
    val okHttpClient = OkHttpClient.Builder()
        .build()

    return HttpClient(engineFactory = OkHttp) {
        engine {
            preconfigured = okHttpClient
        }

        install(ContentNegotiation) {
            json(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }
}