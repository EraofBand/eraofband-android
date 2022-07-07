package com.example.eraofband.remote

import com.example.eraofband.login.GlobalApplication
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModule {
    private var retrofit: Retrofit? = null

    fun getRetrofit(): Retrofit? {
        if (retrofit == null) {
            synchronized(this) {
                retrofit = Retrofit.Builder().baseUrl(GlobalApplication.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build()
            }
        }
        return retrofit
    }
}