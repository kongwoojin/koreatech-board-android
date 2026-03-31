package com.kongjak.koreatechboard.di

import android.os.Build
import com.kongjak.koreatechboard.BuildConfig
import com.kongjak.koreatechboard.data.api.API
import com.kongjak.koreatechboard.util.isRelease
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val API_PRODUCTION = "https://api.koreatech.kongjak.com/v3/"
    private const val API_DEVELOPMENT = "https://api.koreatech.kongjak.com/v3/"

    private val apiUrl = if (isRelease()) {
        API_PRODUCTION
    } else {
        API_DEVELOPMENT
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val userAgentInterceptor = { chain: Interceptor.Chain ->
            val request = chain.request().newBuilder()
                .addHeader(
                    "User-Agent",
                    "Koreatech-Board/${BuildConfig.VERSION_NAME} Android/${Build.VERSION.SDK_INT}"
                )
                .build()
            chain.proceed(request)
        }

        return if (isRelease()) {
            OkHttpClient.Builder()
                .addInterceptor(userAgentInterceptor)
                .build()
        } else {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(userAgentInterceptor)
                .build()
        }
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): API {
        return retrofit.create(API::class.java)
    }
}
