package com.kdh.eatwd.data.util

import com.kdh.eatwd.App
import com.kdh.eatwd.R

object StringUtils {
    fun networkErrorMessage() = App.applicationContext().getString(R.string.message_no_network_connected_str)
    fun etcError() = App.applicationContext().getString(R.string.message_something_went_wrong_str)
}