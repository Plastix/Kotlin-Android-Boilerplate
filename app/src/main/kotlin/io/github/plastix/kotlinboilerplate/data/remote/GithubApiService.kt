package io.github.plastix.kotlinboilerplate.data.remote

import io.github.plastix.kotlinboilerplate.data.remote.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Single

interface GithubApiService {

    @GET("/search/repositories")
    fun repoSearch(@Query("q") query: String,
                   @Query("sort") sort: String,
                   @Query("order") order: String): Single<SearchResponse>
}