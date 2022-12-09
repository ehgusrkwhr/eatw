package com.kdh.eatwd.data.di

import com.kdh.eatwd.data.datasource.*
import com.kdh.eatwd.data.repository.*
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

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository


    @Binds
    @Singleton
    abstract fun bindAirInfoDataSource(airInfoDataSourceImpl: AirInfoDataSourceImpl): AirInfoDataSource

    @Binds
    @Singleton
    abstract fun bindWeatherDataSource(weatherDataSourceImpl: WeatherDataSourceImpl): WeatherDataSource

//    @Binds
//    @Singleton
//    abstract fun bindAddressDataSource(addressDataSourceImpl: AddressDataSourceImpl): AddressDataSource

    @Binds
    @Singleton
    abstract fun bindAddressPagingDataSource(addressDataPaging: AddressDataPaging) : AddressDataPaging

//    @Binds
//    @Singleton
//    abstract fun bindWeatherInfoRepository(weatherInfoRepository: WeatherInfoRepository): WeatherInfoRepository





}