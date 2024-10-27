package com.zandroid.filimo_mvvm.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zandroid.filimo_mvvm.data.db.entity.CategoryEntity
import com.zandroid.filimo_mvvm.data.db.entity.DetailEntity
import com.zandroid.filimo_mvvm.data.db.entity.FavoriteEntity

import com.zandroid.filimo_mvvm.data.db.entity.MovieEntity

@Database([MovieEntity::class,CategoryEntity::class,DetailEntity::class,FavoriteEntity::class], version =6, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class MovieDataBase: RoomDatabase() {
   abstract fun dao():MovieDao
}