package com.zandroid.filimo_mvvm.data.repository

import com.zandroid.filimo_mvvm.data.source.LocalDataSource
import com.zandroid.filimo_mvvm.data.source.RemoteDataSource
import javax.inject.Inject

class HomeRepository @Inject constructor(remoteDataSource: RemoteDataSource,localDataSource: LocalDataSource) {
    val remote=remoteDataSource
    val local=localDataSource
}