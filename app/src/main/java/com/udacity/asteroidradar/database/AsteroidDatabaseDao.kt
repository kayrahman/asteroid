package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*



@Dao
interface AsteroidDatabaseDao{

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids : DatabaseAsteroid)

    @Query("SELECT * FROM asteroid_tbl")
    fun getSavedAsteroids() : LiveData<List<DatabaseAsteroid>>


    @Query("SELECT * FROM asteroid_tbl WHERE close_approach_date LIKE :date ")
    fun getTodayAsteroids(date:String) : LiveData<List<DatabaseAsteroid>>

    @Query("DELETE FROM asteroid_tbl WHERE close_approach_date NOT IN (:dates) ")
    fun deletePreviousDayData(dates : List<String>)

    @Query("SELECT * FROM asteroid_tbl WHERE close_approach_date IN (:dates)")
    fun getWeekAsteroids(dates : List<String>) : LiveData<List<DatabaseAsteroid>>


}

@Dao
interface PodDao{

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertPOD(pod : DatabasePOD) : Long

    @Query("SELECT * FROM tbl_pod")
     fun getPOD() : LiveData<DatabasePOD>



}