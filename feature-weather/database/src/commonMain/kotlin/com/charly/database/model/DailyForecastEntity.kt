package com.charly.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

//  CREATE TABLE daily_weather_table (
//      id INTEGER PRIMARY KEY AUTOINCREMENT,
//      dt INTEGER,
//      sunrise INTEGER,
//      sunset INTEGER,
//      summary TEXT,
//      minTemp TEXT,
//      maxTemp TEXT
//      windSpeed TEXT
//      windDeg TEXT
//      windGust TEXT
//  );
@Entity(
    tableName = "daily_weather_table"
)
@Serializable
data class DailyForecastEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dt: Long? = null,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val summary: String? = null,
    val minTemp: String? = null,
    val maxTemp: String? = null,
    val windSpeed: String? = null,
    val windDeg: String? = null,
    val windGust: String? = null
)
