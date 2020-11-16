package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay


@Entity(tableName = "asteroid_tbl")
data class DatabaseAsteroid constructor(

        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "id")
        val id: Long,

        @ColumnInfo(name = "code_name")
        val codename: String,

        @ColumnInfo(name = "close_approach_date")
        val closeApproachDate: String,

        @ColumnInfo(name = "absolute_magnitude")
        val absoluteMagnitude: Double,

        @ColumnInfo(name = "estimated_diameter")
        val estimatedDiameter: Double,

        @ColumnInfo(name = "relative_velocity")
        val relativeVelocity: Double,

        @ColumnInfo(name = "distance_from_earth")
        val distanceFromEarth: Double,

        @ColumnInfo(name = "is_potentially_hazardous")
        val isPotentiallyHazardous: Boolean
)

fun List<DatabaseAsteroid>.asDomainModel() : List<Asteroid>{
        return map {
            Asteroid(
                    id = it.id,
                    codename = it.codename,
                    closeApproachDate = it.closeApproachDate,
                    absoluteMagnitude = it.absoluteMagnitude,
                    estimatedDiameter = it.estimatedDiameter,
                    relativeVelocity = it.relativeVelocity,
                    distanceFromEarth = it.distanceFromEarth,
                    isPotentiallyHazardous = it.isPotentiallyHazardous
            )

        }
}

@Entity(tableName = "tbl_pod")
data class DatabasePOD constructor(
        @PrimaryKey(autoGenerate = false)
        val id:Long,
        val url : String,
        val title : String
)

fun PictureOfDay.asDatabasePOD() : DatabasePOD{
        return DatabasePOD(
                id = 1,
                title = title,
                url = url
        )
}

fun DatabasePOD.asPicOfDay() : PictureOfDay {
        return PictureOfDay(
                title = title,
                url = url,
                mediaType = Constants.IMAGE
        )
}
