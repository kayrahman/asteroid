package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.work.FetchWeeklyAsteroidDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class AsteroidRadarApplication : Application() {


    val applicationScope = CoroutineScope(Dispatchers.Default)

    private fun workerInit(){
        applicationScope.launch {
            setupWeeklyDataFetchingWorker()
        }
    }

    private fun setupWeeklyDataFetchingWorker() {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setRequiresDeviceIdle(true)
                    }
                }.build()


        val weeklyDataFetchRequest
                = PeriodicWorkRequestBuilder<FetchWeeklyAsteroidDataWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()



        WorkManager.getInstance().enqueueUniquePeriodicWork(
                FetchWeeklyAsteroidDataWorker.WEEKLY_DATA_WORKER,
                ExistingPeriodicWorkPolicy.KEEP,
                weeklyDataFetchRequest)

    }


    override fun onCreate() {
        super.onCreate()

        workerInit()

    }
}