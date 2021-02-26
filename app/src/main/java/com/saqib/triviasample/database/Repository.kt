package com.saqib.triviasample.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.saqib.triviasample.model.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlin.coroutines.CoroutineContext

class Repository(context: Context) : CoroutineScope {

    private val appDatabase = AppDatabase(context)
    private val dao = appDatabase.configurationDao

    override val coroutineContext: CoroutineContext
        get() = IO

    fun insertQueries(queriesModelList: List<Result>) {
        dao?.insertResultsAndQueries(queriesModelList)
    }
    fun deleteData(){
        dao?.deleteQuestionsAndResults()
    }

    fun getAllQuestions(): LiveData<List<Result?>>? {
        return dao?.queriesAndResults
    }

}