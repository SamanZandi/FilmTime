package com.zandroid.filimo_mvvm.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.zandroid.filimo_mvvm.data.models.category.ResponseCategory
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

    //Category
    @TypeConverter
    fun catToJson(category:ResponseCategory):String{
        return gson.toJson(category)
    }

    @TypeConverter
    fun stringToCat(string:String):ResponseCategory{
        return gson.fromJson(string,ResponseCategory::class.java)
    }


}