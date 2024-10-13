package com.zandroid.filimo_mvvm.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zandroid.filimo_mvvm.data.db.entity.MovieEntity
import com.zandroid.filimo_mvvm.data.models.home.ResponseMovie

import com.zandroid.filimo_mvvm.utils.MOVIE_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    //Movies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(entity: MovieEntity)

    @Query("SELECT * FROM ${MOVIE_TABLE_NAME} ORDER BY ID ASC")
    fun loadMovies():Flow<List<MovieEntity>>



}