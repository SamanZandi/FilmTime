package com.zandroid.filimo_mvvm.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.zandroid.filimo_mvvm.data.models.register.ResponseRegister
import com.zandroid.filimo_mvvm.data.models.register.StoredResponseModel
import com.zandroid.filimo_mvvm.data.source.RemoteDataSource
import com.zandroid.filimo_mvvm.utils.EMAIL
import com.zandroid.filimo_mvvm.utils.MESSAGE
import com.zandroid.filimo_mvvm.utils.REGISTER_INFO
import com.zandroid.filimo_mvvm.utils.STATE
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class RegisterRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    @ApplicationContext private val context: Context,
) {
    val remote = remoteDataSource


   companion object StoredKeys{
        val message = stringPreferencesKey(MESSAGE)
        val stateReg = stringPreferencesKey(STATE)
       val email = stringPreferencesKey(EMAIL)
    }



    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(REGISTER_INFO)

    suspend fun saveState(msg:String,state: String,userEmail:String) {
        context.dataStore.edit {preferences->
            preferences[message]=msg
            preferences[stateReg] = state
            preferences[email]=userEmail
        }
    }


    val readRegisterData: Flow<StoredResponseModel> = context.dataStore.data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val message=it[message]?:""
            val state = it[stateReg] ?: "0"
            val email=it[email]?:""
            StoredResponseModel(message,state,email)
        }


    //API
    suspend fun registerUser(name:String,email:String,password:String)= remote.registerUser(name, email, password)


}