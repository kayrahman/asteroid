package com.udacity.asteroidradar.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getInstance
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException
import java.lang.Exception


class FetchWeeklyAsteroidDataWorker(appContext : Context, params : WorkerParameters) :
CoroutineWorker(appContext,params){

    override suspend fun doWork(): Result {
        val database = getInstance(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.fetchAsteroidData()
            repository.deletePreviousDayData()
            Log.d("work_manager_fetch_data","success")
            Result.success()


        }catch (e: Exception){
            Log.d("work_manager_fetch_data",e.toString())
            Result.retry()

        }


    }


    companion object {
        const val WEEKLY_DATA_WORKER = "weekly_data_worker"
    }

}