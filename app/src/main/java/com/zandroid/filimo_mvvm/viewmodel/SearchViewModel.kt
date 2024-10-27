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
class SearchViewModel @Inject constructor(private val repository: HomeRepository): ViewModel(){

    val searchListLiveData=MutableLiveData<NetworkRequest<ResponseMovie>>()

    fun searchMovie(text:String)=viewModelScope.launch {
        searchListLiveData.value=NetworkRequest.Loading()
        val responseSearch=repository.remote.searchMovie(text)
        searchListLiveData.value=NetworkResponse(responseSearch).generalNetworkResponse()
    }

}