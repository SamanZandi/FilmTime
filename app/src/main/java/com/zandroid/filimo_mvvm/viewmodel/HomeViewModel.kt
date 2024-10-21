package com.zandroid.filimo_mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zandroid.filimo_mvvm.data.db.entity.MovieEntity
import com.zandroid.filimo_mvvm.data.models.home.ResponseMovie
import com.zandroid.filimo_mvvm.data.models.register.ResponseRegister
import com.zandroid.filimo_mvvm.data.repository.HomeRepository
import com.zandroid.filimo_mvvm.data.repository.RegisterRepository
import com.zandroid.filimo_mvvm.utils.ANIMATION
import com.zandroid.filimo_mvvm.utils.NetworkRequest
import com.zandroid.filimo_mvvm.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository): ViewModel(){



    //Latest
    val latestLiveData= MutableLiveData<NetworkRequest<ResponseMovie>>()
    //Api
    fun getLatestMovies()=viewModelScope.launch {
        latestLiveData.value=NetworkRequest.Loading()
        val response=repository.remote.getLatestMovies()
        latestLiveData.value=NetworkResponse(response).generalNetworkResponse()
        //Cache
        val cache=latestLiveData.value!!.data
        if (cache!=null){
            offlineLatestMovies(cache)
        }
    }

    //Local
    private fun saveMovie(entity: MovieEntity)=viewModelScope.launch(Dispatchers.IO){
        repository.local.saveMovie(entity)
    }

    val readLatestMoviesFromDb=repository.local.loadMovies().asLiveData()

    private fun offlineLatestMovies(response: ResponseMovie){
        val entity=MovieEntity(0,response)
        saveMovie(entity)
    }

    //All
    val allMovieData= MutableLiveData<NetworkRequest<ResponseMovie>>()
    //Api
    fun getAllMovies()=viewModelScope.launch {
        allMovieData.value=NetworkRequest.Loading()
        val response=repository.remote.getALlMovies()
        allMovieData.value=NetworkResponse(response).generalNetworkResponse()
        //Cache
       val cache=allMovieData.value!!.data
        if (cache!=null){
            offlineAllMovies(cache)
        }
    }

    //Local
    private fun saveAllMovies(entity: MovieEntity)=viewModelScope.launch(Dispatchers.IO){
        repository.local.saveMovie(entity)
    }

    val readAllMoviesFromDb=repository.local.loadMovies().asLiveData()

    private fun offlineAllMovies(response: ResponseMovie){
        val entity=MovieEntity(1,response)
        saveAllMovies(entity)
    }



}