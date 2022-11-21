package com.kdh.eatwd.presenter.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kdh.eatwd.presenter.ui.home.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(saveInstances: Bundle?) {
        super.onCreate(saveInstances)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}