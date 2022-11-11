package com.kdh.eatwd.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class AirStatusResponse(
    val `data`: Data,
    val status: String
) {
    @Serializable
    data class Data(
        val city: String,
        val country: String,
        val current: Current,
        val location: Location,
        val state: String
    ) {
        @Serializable
        data class Current(
            val pollution: Pollution,
            val weather: Weather
        ) {
            @Serializable
            data class Pollution(
                val aqicn: Int,
                val aqius: Int,
                val maincn: String,
                val mainus: String,
                val ts: String
            )
            @Serializable
            data class Weather(
                val hu: Int,
                val ic: String,
                val pr: Int,
                val tp: Int,
                val ts: String,
                val wd: Int,
                val ws: Double
            )
        }
        @Serializable
        data class Location(
            val coordinates: List<Double>,
            val type: String
        )
    }
}
