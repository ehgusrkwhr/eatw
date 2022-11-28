package com.kdh.eatwd.data.entity

import com.google.gson.annotations.SerializedName
import com.kdh.eatwd.presenter.util.Constants



data class WeatherInfoResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    @SerializedName("list") val list: List<WeatherDetail>,
    val message: Int,
    var dayList : List<DayInfo>
) {

    data class DayInfo(
        var day : String,
        var icon : String,
        var tempMin : Double,
        var tempMax : Double
//        val tempMinOrMax : List<Pair<Double,Double>>
    )

    data class City(
        val coord: Coord,
        val country: String,
        val id: Int,
        val name: String,
        val population: Int,
        val sunrise: Int,
        val sunset: Int,
        val timezone: Int
    ) {
        data class Coord(
            val lat: Double,
            val lon: Double
        )
    }

    data class WeatherDetail(
        val clouds: Clouds,
        val dt: Int,
        val dt_txt: String,
        val main: Main,
        val pop: Double,
        val sys: Sys,
        val visibility: Int,
        val weather: List<Weather>,
        val wind: Wind
    ) {
        data class Clouds(
            val all: Int
        )
        data class Main(
            val feels_like: Double,
            val grnd_level: Int,
            val humidity: Int,
            val pressure: Int,
            val sea_level: Int,
            val temp: Double,
            val temp_kf: Double,
            var temp_max: Double,
            var temp_min: Double
        )

        data class Sys(
            val pod: String
        )
        data class Weather(
            val description: String,
            val icon: String,
            val id: Int,
            val main: String
        )
        data class Wind(
            val deg: Int,
            val gust: Double,
            val speed: Double
        )
    }
}