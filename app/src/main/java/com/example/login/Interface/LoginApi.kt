package com.example.login.Interface

import com.example.login.Model.respuesta
import com.example.login.Model.user
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    //@FormUrlEncoded
    //@POST("login/")
    //fun userlogin(
    //    @Field("username") username:String,
     //   @Field("password") password:String
    //)

    @POST("login/")
    suspend fun loginWithCredentials(@Body Login: user): Response<respuesta>
}