package com.zandroid.filimo_mvvm.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.zandroid.filimo_mvvm.data.models.home.ResponseMovie

class TypeConverter {

   private val gson= Gson()

    //Movies
    @TypeConverter
    fun movieToJson(movie:ResponseMovie):String{
        return gson.toJson(movie)
    }

    @TypeConverter
    fun stringToMovie(string:String):ResponseMovie{
        return gson.fromJson(string,ResponseMovie::class.java)
    }


}