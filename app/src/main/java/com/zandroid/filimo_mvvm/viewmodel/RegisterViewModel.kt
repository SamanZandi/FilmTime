package com.zandroid.filimo_mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zandroid.filimo_mvvm.data.models.register.BodyRegister
import com.zandroid.filimo_mvvm.data.models.register.ResponseRegister
import com.zandroid.filimo_mvvm.data.repository.RegisterRepository
import com.zandroid.filimo_mvvm.utils.NetworkRequest
import com.zandroid.filimo_mvvm.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: RegisterRepository):ViewModel(){

    val registerData=MutableLiveData<NetworkRequest<ResponseRegister>>()

    fun sendUserData(name:String,email:String,password:String)=viewModelScope.launch {

        val response = repository.registerUser(name, email, password)
        if(response.body()?.aLLINONEVIDEO?.get(0)?.success=="1") {
            registerData.value = NetworkRequest.Loading()
            registerData.value = NetworkResponse(response).generalNetworkResponse()
        }
    }


    fun saveState(msg:String,state:String,email: String)=viewModelScope.launch{
        repository.saveState(msg, state,email)
    }


    val readRegisterState=repository.readRegisterData



}