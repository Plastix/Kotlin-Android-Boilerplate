package io.github.plastix.kotlinboilerplate.data.remote

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(GithubApiService::class.java)!!

    @Provides @Singleton
    fun provideRetrofit(
            rxJavaCallAdapterFactory: RxJava2CallAdapterFactory,
            gsonConverterFactory: GsonConverterFactory
    ) = Retrofit.Builder()
            .baseUrl(ApiConstants.GITHUB_API_BASE_ENDPOINT)
            .addCallAdapterFactory(rxJavaCallAdapterFactory)
            .addConverterFactory(gsonConverterFactory)
            .build()!!

    @Provides @Singleton
    fun provideGsonConverterFactory() = GsonConverterFactory.create()!!

    @Provides @Singleton
    fun provideRxJavaCallAdapter() = RxJava2CallAdapterFactory.create()!!

}