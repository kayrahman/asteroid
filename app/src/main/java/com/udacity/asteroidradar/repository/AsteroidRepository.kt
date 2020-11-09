package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject


class AsteroidRepository(private val database : AsteroidDatabase) {

    val asteroids : LiveData<List<Asteroid>> = Transformations.map(
            database.asteroidDao.getAsteroids()){
        it.asDomainModel()
    }


    suspend fun fetchAsteroidData(){
        withContext(Dispatchers.IO){

            try {
                val response = AsteroidApi.retrofitService.getAsteroids("2020-11-08", "2020-11-15", Constants.API_KEY).await()
                val responseObject = JSONObject(response)
                val list = parseAsteroidsJsonResult(responseObject)
                Log.d("response",list.size.toString())
                database.asteroidDao.insertAll(*list.asDomainModel())
            }catch (e : Exception){
                Log.d("response",e.toString())
            }

        }
    }


}
