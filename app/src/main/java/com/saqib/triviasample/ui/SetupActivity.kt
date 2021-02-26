package com.saqib.triviasample.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.saqib.triviasample.R
import com.saqib.triviasample.httpClient.ApiClient
import com.saqib.triviasample.utils.GenericViewModelFactory
import kotlinx.android.synthetic.main.setup_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.coroutines.CoroutineContext


class SetupActivity : AppCompatActivity(), CoroutineScope {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setup_activity)
        init()
    }

    private lateinit var level: String
    private fun init() {
        setupViewModel = ViewModelProvider(
            this,
            GenericViewModelFactory(SetupViewModel())
        ).get(SetupViewModel::class.java)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.levels, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_difficulty.adapter = adapter

        spinner_difficulty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                level = when {
                    spinner_difficulty.selectedItem.toString()
                        .toLowerCase(Locale.ROOT) == "easy" ->
                        "easy"

                    spinner_difficulty.selectedItem.toString()
                        .toLowerCase(Locale.ROOT) == "medium" ->
                        "medium"


                    else -> "hard"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Another interface callback
            }

        }
        buttonStart.setOnClickListener {
            if (::level.isInitialized) {
                buttonStart.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                launch { setupViewModel.deleteRecords(this@SetupActivity) }
                callTriviaApi(level)
            } else
                Toast.makeText(this, "Please select the difficulty level", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    private lateinit var setupViewModel: SetupViewModel
    private val backendApi = ApiClient.getInstance()

    private fun callTriviaApi(level: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response =
                kotlin.runCatching {
                    backendApi?.getQueries("10", level)

                }
            withContext(Dispatchers.Main) {
                response.fold(
                    onSuccess = {
                        it?.results?.let { it1 ->
                            setupViewModel.insertAllQuestionsAnswers(
                                this@SetupActivity,
                                it1
                            )
                        }
                        progressBar.visibility = View.GONE
                        buttonStart.visibility = View.VISIBLE
                        startActivity(Intent(this@SetupActivity,QuizActivity::class.java))
                    },
                    onFailure = {
                        Toast.makeText(this@SetupActivity, it.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
}