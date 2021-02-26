package com.saqib.triviasample.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.saqib.triviasample.database.Repository
import com.saqib.triviasample.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SetupViewModel : ViewModel() {

    suspend fun insertAllQuestionsAnswers(context: Context, queries: List<Result>) {
        withContext(Dispatchers.IO) {
            Repository(context).insertQueries(queries)
        }
    }

    fun deleteRecords(context: Context) {
        Repository(context).deleteData()
    }

    fun getAllQueries(context: Context): LiveData<List<Result?>>? {
        return Repository(context).getAllQuestions()
    }

}