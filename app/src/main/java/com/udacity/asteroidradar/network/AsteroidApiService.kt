package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface AsteroidApiService {

    /*@GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
            @Query("start_date")start_date:String,
            @Query("end_date")end_date:String,
            @Query("api_key")api_key:String) : String*/

    @GET("neo/rest/v1/feed")
     fun getAsteroids(
            @Query("start_date")start_date:String,
            @Query("end_date")end_date:String,
            @Query("api_key")api_key:String) : Deferred<String>


}

private val retrofit = Retrofit.Builder()
       // .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()


object AsteroidApi{
    val retrofitService : AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}