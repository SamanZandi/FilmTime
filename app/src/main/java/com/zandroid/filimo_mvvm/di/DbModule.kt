package com.zandroid.filimo_mvvm.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zandroid.filimo_mvvm.data.db.MovieDataBase
import com.zandroid.filimo_mvvm.utils.MOVIE_DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context:Context)=
        Room.databaseBuilder(context,MovieDataBase::class.java, MOVIE_DB_NAME)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideDao(db:MovieDataBase)=db.dao()


}