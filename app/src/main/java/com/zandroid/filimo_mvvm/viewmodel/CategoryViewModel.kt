package com.zandroid.filimo_mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zandroid.filimo_mvvm.data.db.entity.CategoryEntity
import com.zandroid.filimo_mvvm.data.db.entity.MovieEntity
import com.zandroid.filimo_mvvm.data.models.category.ResponseCategory
import com.zandroid.filimo_mvvm.data.models.home.ResponseMovie
import com.zandroid.filimo_mvvm.data.models.register.ResponseRegister
import com.zandroid.filimo_mvvm.data.repository.HomeRepository
import com.zandroid.filimo_mvvm.data.repository.RegisterRepository
import com.zandroid.filimo_mvvm.utils.ANIMATION
import com.zandroid.filimo_mvvm.utils.CAT_TITLE_KEY
import com.zandroid.filimo_mvvm.utils.NetworkRequest
import com.zandroid.filimo_mvvm.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel(){



    //get Categories
    val categoriesLiveData= MutableLiveData<NetworkRequest<ResponseCategory>>()
    //Api
    fun getCategoryList()=viewModelScope.launch {
        categoriesLiveData.value=NetworkRequest.Loading()
        val response=repository.remote.getCategories()
        categoriesLiveData.value=NetworkResponse(response).generalNetworkResponse()

        //cache
        val cache=categoriesLiveData.value!!.data
        if (cache!=null) loadOfflineCategories(cache)
    }

    //Local
    private fun saveCategory(entity: CategoryEntity)=viewModelScope.launch {
        repository.local.saveCategory(entity)
    }

    val readCategoriesFromDb= repository.local.loadCategories().asLiveData()

    private fun loadOfflineCategories(responseCategory: ResponseCategory)=viewModelScope.launch {
        val entity=CategoryEntity(0,responseCategory)
        saveCategory(entity)
    }



}