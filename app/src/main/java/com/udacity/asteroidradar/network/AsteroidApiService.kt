package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface AsteroidApiService {

    @GET("neo/rest/v1/feed")
     fun getAsteroids(
            @Query("api_key")api_key:String) : Deferred<String>


    @GET("neo/rest/v1/feed")
    fun getTodayAsteroids(
            @Query("start_date")start_date:String,
            @Query("end_date")end_date:String,
            @Query("api_key")api_key:String) : Deferred<String>


}


interface PictureApiService {

    @GET("planetary/apod")
    fun getPictureOfTheDay(
        @Query("api_key") api_key : String,
        @Query("date")date : String

    ): Deferred<PictureOfDay>

}


private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofitAsteroid = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()


private val retrofitPicture = Retrofit.Builder()
     .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()



object AsteroidApi{
    val retrofitService : AsteroidApiService by lazy {
        retrofitAsteroid.create(AsteroidApiService::class.java)
    }
}


object PictureApi{
    val retrofitService : PictureApiService by lazy {
        retrofitPicture.create(PictureApiService::class.java)
    }
}