package com.saqib.triviasample.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.saqib.triviasample.model.Result

@Dao
interface ConfigurationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertResultsAndQueries(queryModel: List<Result>)

    @get:Query("select * from QuestionsAndResults")
    val queriesAndResults: LiveData<List<Result?>>?

    @Query("DELETE FROM QuestionsAndResults")
    fun deleteQuestionsAndResults()
}
