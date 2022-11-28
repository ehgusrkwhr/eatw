package com.kdh.eatwd.presenter.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kdh.eatwd.presenter.ui.home.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(saveInstances: Bundle?) {
//        installSplashScreen()
        super.onCreate(saveInstances)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        },500)

    }
}