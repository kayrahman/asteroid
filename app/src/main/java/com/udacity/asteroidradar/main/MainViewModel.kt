package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getInstance
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(val app: Application) : ViewModel() {

    val database = getInstance(app)
    val repository = AsteroidRepository(database)

    init {
        viewModelScope.launch {
            repository.fetchAsteroidData()
        }
    }

    val asteroids = repository.asteroids

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}