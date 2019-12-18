package com.example.login.Interface

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object LoginService {
    var BASE_URL = "http://petroperu.gpspetroperu.com/api/authentication/"
    fun makeLoginService(): LoginApi {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(LoginApi::class.java)
    }
}