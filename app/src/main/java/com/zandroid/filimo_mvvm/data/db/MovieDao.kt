package com.zandroid.filimo_mvvm.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zandroid.filimo_mvvm.data.db.entity.CategoryEntity
import com.zandroid.filimo_mvvm.data.db.entity.DetailEntity
import com.zandroid.filimo_mvvm.data.db.entity.FavoriteEntity
import com.zandroid.filimo_mvvm.data.db.entity.MovieEntity
import com.zandroid.filimo_mvvm.data.models.home.ResponseMovie
import com.zandroid.filimo_mvvm.utils.CATEGORY_TABLE_NAME
import com.zandroid.filimo_mvvm.utils.DETAIL_TABLE_NAME
import com.zandroid.filimo_mvvm.utils.FAVORITE_TABLE

import com.zandroid.filimo_mvvm.utils.MOVIE_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    //Movies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(entity: MovieEntity)

    @Query("SELECT * FROM ${MOVIE_TABLE_NAME} ORDER BY ID ASC")
    fun loadMovies():Flow<List<MovieEntity>>

    //Category
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCategory(entity: CategoryEntity)

    @Query("SELECT * FROM ${CATEGORY_TABLE_NAME} ORDER BY ID ASC")
    fun loadCategories():Flow<List<CategoryEntity>>


    //Details
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDetail(entity: DetailEntity)

    @Query("SELECT * FROM ${DETAIL_TABLE_NAME} WHERE id=:id")
    fun loadDetails(id:Int):Flow<DetailEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM ${DETAIL_TABLE_NAME} WHERE ID=:id)")
    fun existsInCache(id:Int):Flow<Boolean>

    //Favorite
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavorite(entity: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(entity: FavoriteEntity)

    @Query("SELECT * FROM ${FAVORITE_TABLE} ORDER BY ID ASC")
    fun loadFavoriteList():Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS (SELECT 1 FROM ${FAVORITE_TABLE} WHERE ID=:id)")
    fun existsFavoriteInCache(id:Int):Flow<Boolean>


}