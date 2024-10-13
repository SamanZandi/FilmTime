package com.zandroid.filimo_mvvm.data.server

import com.zandroid.filimo_mvvm.data.models.home.ResponseMovie
import com.zandroid.filimo_mvvm.data.models.register.ResponseRegister
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServices {


    @FormUrlEncoded
    @POST("api.php?user_register")
    suspend fun postRegisterUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<ResponseRegister>

    /*    @POST("api.php?user_register")
        suspend fun postRegisterUser(@Body body:BodyRegister):Response<ResponseRegister>*/

    @GET("api.php?latest_video")
    suspend fun getLatestMovies(): Response<ResponseMovie>

    @GET("api.php?All_videos")
    suspend fun getALlMovies(): Response<ResponseMovie>

}