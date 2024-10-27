package com.zandroid.filimo_mvvm.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zandroid.filimo_mvvm.data.models.detail.ResponseSingleMovie
import com.zandroid.filimo_mvvm.data.models.home.ResponseMovie
import com.zandroid.filimo_mvvm.utils.DETAIL_TABLE_NAME
import com.zandroid.filimo_mvvm.utils.FAVORITE_TABLE

import com.zandroid.filimo_mvvm.utils.MOVIE_TABLE_NAME

@Entity(tableName = FAVORITE_TABLE)
data class FavoriteEntity (
    @PrimaryKey(autoGenerate = false)
    var id:Int,
    var response: ResponseSingleMovie
)