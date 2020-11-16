package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getInstance
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(val app: Application) : ViewModel() {

    val database = getInstance(app)
    val repository = AsteroidRepository(database)

    private val _navigateToSelectedAsteroidDetail = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroidDetail : LiveData<Asteroid>
    get() = _navigateToSelectedAsteroidDetail

    private val dataType = MutableLiveData<String>("")

    init {
        viewModelScope.launch {
            repository.fetchPictureOfTheDay()
            repository.fetchAsteroidData()

        }
    }

    val asteroids = dataType.switchMap {

        when(it){
            MainFragment.FetchType.TODAY.name ->  repository.asteroidsToday
            MainFragment.FetchType.WEEKLY.name -> repository.weeklyAsteroids
            MainFragment.FetchType.ALL.name -> repository.savedAsteroids
            else -> {
                repository.savedAsteroids
            }
        }


    }

    val picOfDay = repository.pics


   fun updateAsteroidDataType(type : String){
       dataType.value = type
   }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }


    fun displayAsteroidDetail(asteroid: Asteroid){
        _navigateToSelectedAsteroidDetail.value = asteroid
    }

    fun displayAsteroidDetailComplete(){
        _navigateToSelectedAsteroidDetail.value = null
    }


}