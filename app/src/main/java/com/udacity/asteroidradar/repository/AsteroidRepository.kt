package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDatabasePOD
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.database.asPicOfDay
import com.udacity.asteroidradar.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject


class AsteroidRepository(private val database : AsteroidDatabase) {

    val savedAsteroids : LiveData<List<Asteroid>> = Transformations.map(
            database.asteroidDao.getSavedAsteroids()){
        it.asDomainModel()
    }


    val weeklyAsteroids : LiveData<List<Asteroid>> = Transformations.map(
            database.asteroidDao.getWeekAsteroids(getNextSevenDaysFormattedDates())){
        it.asDomainModel()
    }

    val asteroidsToday : LiveData<List<Asteroid>> = Transformations.map(
            database.asteroidDao.getTodayAsteroids(getTodayFormattedDate())){
        it.asDomainModel()
    }



    val pics : LiveData<PictureOfDay?> = Transformations.map(
            database.podDao.getPOD()){
        it?.asPicOfDay()
    }




    suspend fun fetchPictureOfTheDay(){
        withContext(Dispatchers.IO){
            try {
                val pod = PictureApi.retrofitService.getPictureOfTheDay(Constants.API_KEY,"2020-11-06").await()
                when (pod.mediaType) {
                    Constants.IMAGE -> {
                        database.podDao.insertPOD(pod.asDatabasePOD())
                        Log.d("response_pod","image")
                    }
                    else ->{
                        Log.d("response_pod","Not Image")
                    }
                }

            }catch (e:Exception){
                Log.d("response_pod",e.toString())
            }
        }
    }

    fun deletePreviousDayData(){
         database.asteroidDao.deletePreviousDayData(getNextSevenDaysFormattedDates())
    }


    suspend fun fetchAsteroidData(){
        withContext(Dispatchers.IO){

            try {
                val response = AsteroidApi.retrofitService.getAsteroids( Constants.API_KEY).await()
                val responseObject = JSONObject(response)
                val list = parseAsteroidsJsonResult(responseObject)
                Log.d("response_list",list.size.toString())
                database.asteroidDao.insertAll(*list.asDomainModel())
            }catch (e : Exception){
                Log.d("response_list",e.toString())
            }

        }
    }


}
