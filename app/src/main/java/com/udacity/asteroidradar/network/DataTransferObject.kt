package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.DatabaseAsteroid


fun ArrayList<Asteroid>.asDomainModel() : Array<DatabaseAsteroid>{
    return map {
        DatabaseAsteroid(
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth = it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}