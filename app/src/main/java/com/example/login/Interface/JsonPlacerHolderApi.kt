package com.example.d10.Interface

import com.example.d10.Model.Photos
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface JsonPlacerHolderApi {

    @GET("photos/{id}")
    fun getPostsItem(@Path("id") id: Int): Call<Photos>


    @GET("photos")
    fun getPosts(): Call<List<Photos>>



    @DELETE("photos/{id}")
    fun deletePost(@Path("id") id: Int): Call<List<Photos>>

    @PUT("photos/{id}")
    fun updatePost(@Path("id")id: Int, @Body Photos: Photos)


    companion object {

        var BASE_URL = "https://jsonplaceholder.typicode.com/"

        fun create() : JsonPlacerHolderApi {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(JsonPlacerHolderApi::class.java)

        }
    }
}