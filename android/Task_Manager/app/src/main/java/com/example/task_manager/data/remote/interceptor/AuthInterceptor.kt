package com.example.task_manager.data.remote.interceptor

import com.example.task_manager.data.datastore.UserPreferences
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private  val userPreferences: UserPreferences
) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { userPreferences.token.firstOrNull() }

        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}