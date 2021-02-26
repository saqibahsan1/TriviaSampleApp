package com.saqib.triviasample.httpClient
import com.saqib.triviasample.model.QueriesModel
import retrofit2.Call
import retrofit2.http.*

interface HttpEndPoints {

    @GET("api.php")
    suspend fun getQueries(
        @Query("amount") amount: String,
        @Query("difficulty") difficulty: String):
            QueriesModel
}