package com.kdh.eatwd.presenter.util

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import com.kdh.eatwd.App
import com.kdh.eatwd.R
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.*


//date util
fun Date.dateToString(format: String, local: Locale = Locale.getDefault()): String = SimpleDateFormat(format, local).format(this)
fun currentDate(): Date = Calendar.getInstance().time
fun getDayOfWeek(date: String): String = LocalDate.parse(date).dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)

fun getTimeOfDay(time: String): String {
    val day = if (LocalTime.parse(time).hour < 12) {
        "오전"
    } else {
        "오후"
    }
    return App.applicationContext().getString(R.string.today_show_time, day, LocalTime.parse(time).hour)
}

fun getCurrentTime(): Int {
    return LocalTime.now().hour
}

fun getTimeFromDateString(date: String): Int {
    return LocalTime.parse(date).hour
}

// math
fun getAverage(sum: Double, size: Int): Double = sum / size

// temp
fun convertKelvinToCelsius(kelvin: Double): Int = (kelvin - Constants.CELSIUS_VALUE).toInt()


// file
fun Activity.captureScreen(): String {
    val cacheFilePath = cacheDir.absolutePath + "/capture.png"
    val root: View = this.window.decorView.rootView
    val bitmap: Bitmap = Bitmap.createBitmap(root.width, root.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    root.draw(canvas)

    val imageFile = File(cacheFilePath)
    val outputStream = FileOutputStream(imageFile)

    outputStream.use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
    }

    return cacheFilePath
}



