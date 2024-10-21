package com.zandroid.filimo_mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
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
    }




}