package com.zandroid.filimo_mvvm.data.source

import com.zandroid.filimo_mvvm.data.db.MovieDao
import com.zandroid.filimo_mvvm.data.db.entity.MovieEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: MovieDao) {
    suspend fun saveMovie(entity: MovieEntity)=dao.saveMovie(entity)
    fun loadMovies()=dao.loadMovies()

}