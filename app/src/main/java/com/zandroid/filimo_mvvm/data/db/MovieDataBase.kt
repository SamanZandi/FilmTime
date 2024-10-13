package com.zandroid.filimo_mvvm.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.zandroid.filimo_mvvm.data.db.entity.MovieEntity

@Database([MovieEntity::class], version = 3, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class MovieDataBase: RoomDatabase() {
   abstract fun dao():MovieDao
}