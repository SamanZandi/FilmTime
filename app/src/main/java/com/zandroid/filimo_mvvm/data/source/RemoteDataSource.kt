package com.zandroid.filimo_mvvm.data.source

import com.zandroid.filimo_mvvm.data.models.register.BodyRegister
import com.zandroid.filimo_mvvm.data.server.ApiServices
import retrofit2.http.Field
import retrofit2.http.Query
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val api:ApiServices) {

    suspend fun registerUser(name:String,email:String,password:String)= api.postRegisterUser(name, email, password)
    suspend fun getLatestMovies()=api.getLatestMovies()
    suspend fun getALlMovies()=api.getALlMovies()
    suspend fun getCategories()=api.getCategories()
    suspend fun getMoviesByCategory(catId:Int)=api.getMoviesByCategory(catId)
    suspend fun getMovieDetails(id:Int)=api.getMovieDetails(id)
}