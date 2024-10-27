package com.zandroid.filimo_mvvm.data.server

import com.zandroid.filimo_mvvm.data.models.category.ResponseCategory
import com.zandroid.filimo_mvvm.data.models.detail.ResponseSingleMovie
import com.zandroid.filimo_mvvm.data.models.home.ResponseMovie
import com.zandroid.filimo_mvvm.data.models.register.ResponseRegister
import org.w3c.dom.Text
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServices {


    @FormUrlEncoded
    @POST("api.php?user_register")
    suspend fun postRegisterUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<ResponseRegister>



    @GET("api.php?latest_video")
    suspend fun getLatestMovies(): Response<ResponseMovie>

    @GET("api.php?All_videos")
    suspend fun getALlMovies(): Response<ResponseMovie>


    @GET("api.php?cat_list")
    suspend fun getCategories(): Response<ResponseCategory>

    @GET("api.php?")
    suspend fun getMoviesByCategory(@Query("cat_id") catId:Int): Response<ResponseMovie>


    @GET("api.php?")
    suspend fun getMovieDetails(@Query("video_id") id:Int): Response<ResponseSingleMovie>

    @GET("api.php")
    suspend fun searchMovie(@Query("search_text") text:String): Response<ResponseMovie>

}