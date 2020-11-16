package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getInstance
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException
import java.lang.Exception


class DeletePreviousDayAsteroidData(appContext: Context, params: WorkerParameters):
CoroutineWorker(appContext,params){
    override suspend fun doWork(): Result {

        val database = getInstance(applicationContext)
        val reposirtoy = AsteroidRepository(database)

       return try {
            reposirtoy.deletePreviousDayData()
            Result.success()
        }catch (e:HttpException){
            Result.retry()
        }
    }
}