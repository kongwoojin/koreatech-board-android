package com.kongjak.koreatechboard.di

import com.kongjak.koreatechboard.util.BuildType
import com.kongjak.koreatechboard.util.getBuildInfo
import com.kongjak.koreatechboard.util.getPlatformInfo
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val API_PRODUCTION = "https://api.koreatech.kongjak.com/v3/"
private const val API_DEVELOPMENT = "https://api.koreatech.kongjak.com/v3/"

private val apiUrl = if (getBuildInfo().buildType is BuildType.Release) {
    API_PRODUCTION
} else {
    API_DEVELOPMENT
}

fun provideHttpClient(): HttpClient {
    return HttpClient(httpClientEngine()) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            json(Json)
        }

        install(DefaultRequest) {
            url(apiUrl)
            header("User-Agent", "Koreatech-Board/${getBuildInfo().applicationVersion} ${getPlatformInfo().os}")
        }
    }
}

expect fun httpClientEngine(): HttpClientEngine
