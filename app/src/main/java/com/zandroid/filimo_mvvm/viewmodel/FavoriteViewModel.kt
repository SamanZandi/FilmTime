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
class FavoriteViewModel @Inject constructor(repository: HomeRepository): ViewModel(){

    val readFavoriteData=repository.local.loadFavoriteList().asLiveData()

}