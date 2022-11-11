package com.kdh.eatwd.data.di

import com.kdh.eatwd.data.repository.AirInfoRepository
import com.kdh.eatwd.data.repository.AirInfoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAirInfoRepository(airInfoRepositoryImpl: AirInfoRepositoryImpl): AirInfoRepository


}