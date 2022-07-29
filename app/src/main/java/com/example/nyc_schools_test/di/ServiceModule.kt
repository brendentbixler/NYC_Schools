package com.example.nyc_schools_test.di

import com.example.nyc_schools_test.common.BASE_URL
import com.example.nyc_schools_test.model.remote.Api
import com.example.nyc_schools_test.model.remote.Repository
import com.example.nyc_schools_test.model.remote.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Makes network calls using retrofit
 */
@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    fun provideNycService(): Api =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)

    @Provides
    fun provideRepositoryLayer(service: Api): Repository =
        RepositoryImpl(service)

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideCoroutineScope(dispatcher: CoroutineDispatcher): CoroutineScope =
        CoroutineScope(dispatcher)
}