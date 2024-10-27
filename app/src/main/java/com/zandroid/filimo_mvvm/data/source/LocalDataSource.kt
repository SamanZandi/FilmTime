package com.zandroid.filimo_mvvm.data.source

import com.zandroid.filimo_mvvm.data.db.MovieDao
import com.zandroid.filimo_mvvm.data.db.entity.CategoryEntity
import com.zandroid.filimo_mvvm.data.db.entity.DetailEntity
import com.zandroid.filimo_mvvm.data.db.entity.FavoriteEntity
import com.zandroid.filimo_mvvm.data.db.entity.MovieEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: MovieDao) {
    //movies
    suspend fun saveMovie(entity: MovieEntity)=dao.saveMovie(entity)
    fun loadMovies()=dao.loadMovies()
    //category
    suspend fun saveCategory(entity: CategoryEntity)=dao.saveCategory(entity)
    fun loadCategories()=dao.loadCategories()

    //Details
    suspend fun saveDetail(entity: DetailEntity)=dao.saveDetail(entity)
    fun loadDetails(id:Int)=dao.loadDetails(id)
    fun existsInCache(id:Int)=dao.existsInCache(id)

    //Favorite
    suspend fun saveFavorite(entity: FavoriteEntity)=dao.saveFavorite(entity)
    suspend fun deleteFavorite(entity: FavoriteEntity)=dao.deleteFavorite(entity)
    fun loadFavoriteList()=dao.loadFavoriteList()
    fun existsFavoriteInCache(id:Int)=dao.existsFavoriteInCache(id)
}