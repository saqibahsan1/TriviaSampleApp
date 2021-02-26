package com.saqib.triviasample.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.saqib.triviasample.R
import kotlinx.android.synthetic.main.congrats_activity.*


class Congrats : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.congrats_activity)
        init()
    }

    @SuppressLint("SetTextI18n")
    private fun init() {

        val intent = intent
        val score = intent.getIntExtra(QuizActivity.finalScore, 1)
        if (score > 6) {
            Glide.with(this)
                .load("https://media.giphy.com/media/3oFzmiMu3v4LIXpJBK/giphy.gif")
                .placeholder(R.drawable.tivia)
                .into(imageViewGif)
        }else
            Glide.with(this)
                .load("https://media.giphy.com/media/Kg9ST0J3hL8PhCr0kZ/giphy.gif")
                .placeholder(R.drawable.tivia)
                .into(imageViewGif)
        yourScore.text = "You have scored $score"
    }

}
