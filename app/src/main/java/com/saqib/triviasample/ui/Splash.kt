package com.saqib.triviasample.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.saqib.triviasample.R
import kotlinx.android.synthetic.main.splash.*

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        init()
    }

    private fun init(){
        logo.startAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.zoom_in
            )
        )

        Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@Splash, SetupActivity::class.java))
            finish()
        }, 2000)
    }
}