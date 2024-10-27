package com.zandroid.filimo_mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zandroid.filimo_mvvm.data.db.entity.DetailEntity
import com.zandroid.filimo_mvvm.data.db.entity.FavoriteEntity
import com.zandroid.filimo_mvvm.data.db.entity.MovieEntity
import com.zandroid.filimo_mvvm.data.models.detail.ResponseSingleMovie
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
class DetailViewModel @Inject constructor(private val repository: HomeRepository): ViewModel(){

    //Latest
    val detailLiveData= MutableLiveData<NetworkRequest<ResponseSingleMovie>>()
    //Api
    fun loadDetailsMovie(id:Int)=viewModelScope.launch {
        detailLiveData.value=NetworkRequest.Loading()
        val response=repository.remote.getMovieDetails(id)
        detailLiveData.value=NetworkResponse(response).generalNetworkResponse()
        //cache
        val cache=detailLiveData.value!!.data
        if (cache!=null)
            cacheOfflineDetails(cache.aLLINONEVIDEO.get(0).id!!.toInt(),cache)
    }

    //Local

    //Details
    private fun saveDetails(entity: DetailEntity)=viewModelScope.launch{
        repository.local.saveDetail(entity)
    }
    fun readDetailsFromDb(id:Int)=repository.local.loadDetails(id).asLiveData()
    private fun cacheOfflineDetails(id:Int,response:ResponseSingleMovie){
        val entity=DetailEntity(id,response)
        saveDetails(entity)
    }
    val existsDetailsLiveData=MutableLiveData<Boolean>()
    fun existsDetails(id:Int)=viewModelScope.launch{
        repository.local.existsInCache(id).collect{
            existsDetailsLiveData.value=it
        }
    }

    //Favorites
     fun saveFavorite(entity: FavoriteEntity)=viewModelScope.launch {
        repository.local.saveFavorite(entity)
    }

     fun deleteFavorite(entity: FavoriteEntity)=viewModelScope.launch {
        repository.local.deleteFavorite(entity)
    }

    val existFavoriteData=MutableLiveData<Boolean>()
    fun existInFavorite(id:Int)=viewModelScope.launch {
       repository.local.existsFavoriteInCache(id).collect{
           existFavoriteData.value=it
       }
    }





}