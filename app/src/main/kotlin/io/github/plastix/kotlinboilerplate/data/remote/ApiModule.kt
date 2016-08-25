package io.github.plastix.kotlinboilerplate.data.remote

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object ApiModule {

    @Provides @Singleton @JvmStatic
    fun provideApiService(retrofit: Retrofit): GithubApiService {
        return retrofit.create(GithubApiService::class.java)
    }

    @Provides @Singleton @JvmStatic
    fun provideRetrofit(rxJavaCallAdapterFactory: RxJavaCallAdapterFactory,
                        gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(ApiConstants.GITHUB_API_BASE_ENDPOINT)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
                .build()

    }

    @Provides @Singleton @JvmStatic
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides @Singleton @JvmStatic
    fun provideRxJavaCallAdapter(): RxJavaCallAdapterFactory {
        return RxJavaCallAdapterFactory.create()
    }

}