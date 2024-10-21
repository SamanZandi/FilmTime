package com.zandroid.filimo_mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zandroid.filimo_mvvm.data.models.home.ResponseMovie
import com.zandroid.filimo_mvvm.data.repository.HomeRepository
import com.zandroid.filimo_mvvm.utils.NetworkRequest
import com.zandroid.filimo_mvvm.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieByCatViewModel @Inject constructor(private val repository: HomeRepository): ViewModel(){
    //Latest
    val movieCategoryData= MutableLiveData<NetworkRequest<ResponseMovie>>()
    //Api
    fun getMoviesByCategories(catId:Int)=viewModelScope.launch {
        movieCategoryData.value=NetworkRequest.Loading()
        val response=repository.remote.getMoviesByCategory(catId)
        movieCategoryData.value=NetworkResponse(response).generalNetworkResponse()

    }

 




}