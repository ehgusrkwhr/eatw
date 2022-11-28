package com.kdh.eatwd.presenter.util

import com.kdh.eatwd.App
import com.kdh.eatwd.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.*


//date util
fun Date.dateToString(format: String, local: Locale = Locale.getDefault()): String = SimpleDateFormat(format, local).format(this)
fun currentDate(): Date = Calendar.getInstance().time
fun getDayOfWeek(date: String) = LocalDate.parse(date).dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)

fun getTimeOfDay(time: String): String {
    val day = if (LocalTime.parse(time).hour < 12) {
        "오전"
    } else {
        "오후"
    }
    return App.applicationContext().getString(R.string.today_show_time, day, LocalTime.parse(time).hour)
}




// math
fun getAverage(sum: Double, size: Int): Double = sum / size

// temp
fun convertKelvinToCelsius(kelvin: Double): Int = (kelvin - Constants.CELSIUS_VALUE).toInt()



