package com.saqib.triviasample.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.saqib.triviasample.R
import com.saqib.triviasample.model.Result
import com.saqib.triviasample.utils.GenericViewModelFactory
import kotlinx.android.synthetic.main.quiz_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

@Suppress("UNCHECKED_CAST")
class QuizActivity : AppCompatActivity(), CoroutineScope, View.OnClickListener {

    private lateinit var setupViewModel: SetupViewModel
    private var mQuestionList: ArrayList<Result>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_activity)
        init()
    }

    private fun init() {
        setupViewModel = ViewModelProvider(
            this,
            GenericViewModelFactory(SetupViewModel())
        ).get(SetupViewModel::class.java)

        setupViewModel.getAllQueries(this)?.observe(this, {
            mQuestionList = it as ArrayList<Result>?
            setUpQueries()
        })
        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)
        btn_submit.setOnClickListener(this)

    }

    private var mCurrentPosition: Int = 1
    private var correctAnswer: Int = 0

    @SuppressLint("SetTextI18n")
    private fun setQuestion() {
        val question = mQuestionList!![mCurrentPosition - 1]
        defaultOptionsView()
        if (mCurrentPosition == mQuestionList!!.size) {
            btn_submit.text = "Finish"
        } else {
            btn_submit.text = "Submit"
        }

        progressBar.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition" + "/" + progressBar.max
        if (question.type == "boolean") {
            tv_option_three.visibility = View.GONE
            tv_option_four.visibility = View.GONE
            tv_question.text = question.question
            tv_option_one.text = question.incorrect_answers!![0]
            tv_option_two.text = question.correct_answer
            correctAnswer = 2
        } else {
            tv_option_three.visibility = View.VISIBLE
            tv_option_four.visibility = View.VISIBLE
            tv_question.text = question.question
            tv_option_one.text = question.incorrect_answers!![0]
            tv_option_two.text = question.incorrect_answers!![1]
            tv_option_three.text = question.incorrect_answers!![2]
            tv_option_four.text = question.correct_answer
            correctAnswer = 4
        }
        timeLeft = countDownTimerL
        startCountDown()
    }

    companion object {
        const val finalScore = "FinalScore"
        private const val countDownTimerL: Long = 15000
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(0, tv_option_one)
        options.add(1, tv_option_two)
        options.add(2, tv_option_three)
        options.add(3, tv_option_four)
        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }
    }


    override val coroutineContext: CoroutineContext
        get() = IO

    private lateinit var answer: String

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_option_one -> {
                selectedOptionView(tv_option_one, 1)
                answer = tv_option_one.text.toString()
            }
            R.id.tv_option_two -> {
                selectedOptionView(tv_option_two, 2)
                answer = tv_option_two.text.toString()
                score++
                val question = mQuestionList!![mCurrentPosition - 1]
                when (question.difficulty) {
                    "hard" -> {
                        score + 2
                        scoreText.text = "Score: $score"
                    }
                    "medium" -> {
                        score + 1
                        scoreText.text = "Score: $score"
                    }
                    else -> scoreText.text = "Score: $score"
                }
            }
            R.id.tv_option_three -> {
                selectedOptionView(tv_option_three, 3)
                answer = tv_option_three.text.toString()
            }
            R.id.tv_option_four -> {
                selectedOptionView(tv_option_four, 4)
                answer = tv_option_four.text.toString()
                score++
                val question = mQuestionList!![mCurrentPosition - 1]
                when (question.difficulty) {
                    "hard" -> {
                        score += 2
                        scoreText.text = "Score: $score"
                    }
                    "medium" -> {
                        score + 1
                        scoreText.text = "Score: $score"
                    }
                    else -> scoreText.text = "Score: $score"
                }
            }
            R.id.btn_submit -> {
                if (::answer.isInitialized) {
                    if (mSelectedOptionPosition == 0)
                        mCurrentPosition++
                    setUpQueries()
                } else
                    Toast.makeText(this, "Please select any of the option!", Toast.LENGTH_SHORT)
                        .show()
            }
        }
    }

    private var countDownTimer: CountDownTimer? = null
    private var timeLeft: Long = 0
    private fun startCountDown() {
        countDownTimer = object : CountDownTimer(timeLeft, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                updateCountDown()
            }

            override fun onFinish() {
                timeLeft = 0
                updateCountDown()
                check()
            }
        }.start()
    }

    private var colorStateListCountDown: ColorStateList? = null
    private fun updateCountDown() {
        val min = (timeLeft / 1000).toInt() / 60
        val sec = (timeLeft / 1000).toInt() % 60
        colorStateListCountDown = timeCounter.textColors
        val timeFormat = String.format(Locale.getDefault(), "%02d:%02d", min, sec)
        timeCounter.text = timeFormat

        if (timeLeft < 10000) {
            timeCounter.setTextColor(Color.RED)
        } else {
            timeCounter.setTextColor(colorStateListCountDown)
        }

    }

    private fun setUpQueries() {
        if (mSelectedOptionPosition == 0) {
            when {
                mCurrentPosition <= mQuestionList!!.size -> {
                    setQuestion()
                }
                else -> {
                    Toast.makeText(
                        this,
                        "You have successfully completed the Quiz", Toast.LENGTH_SHORT
                    ).show()
                   finishQuizActivity()
                }
            }
        } else {
            check()
        }
    }

    private var mSelectedOptionPosition: Int = 0
    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                tv_option_one.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            2 -> {
                tv_option_two.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            3 -> {
                tv_option_three.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            4 -> {
                tv_option_four.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
        }
    }

    private fun finishQuizActivity() {
        startActivity(Intent(this, Congrats::class.java).putExtra(finalScore,score))
        finish()
    }

    private var score: Int = 0
    @SuppressLint("SetTextI18n")
    private fun check() {
        val question = mQuestionList?.get(mCurrentPosition - 1)
        countDownTimer?.cancel()
        if (question!!.correct_answer != answer) {
            answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
        }
        answerView(correctAnswer, R.drawable.correct_option_border_bg)
        if (mCurrentPosition == mQuestionList!!.size) {
            btn_submit.text = "Finish"
        } else {
            btn_submit.text = "Go to next question"
        }
        mSelectedOptionPosition = 0
    }
    private fun resetField(target: Any, fieldName: String) {
        val field = target.javaClass.getDeclaredField(fieldName)

        with (field) {
            isAccessible = true
            set(target, null)
        }
    }
}