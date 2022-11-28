package com.kdh.eatwd.presenter.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

object UiUtils {
    fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    fun pxToDp(context: Context,px : Int) : Int{
        val density = context.resources.displayMetrics.density
        return (px / density).toInt()
    }





}