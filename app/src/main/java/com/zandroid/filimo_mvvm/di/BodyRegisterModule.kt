package com.zandroid.filimo_mvvm.di

import com.zandroid.filimo_mvvm.data.models.register.BodyRegister
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object BodyRegisterModule {

    @Provides
    @Singleton
    fun provideBody()=BodyRegister()
}