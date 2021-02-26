package com.saqib.triviasample.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "QuestionsAndResults")
class Result {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var category: String =""
    var correct_answer: String =""
    var difficulty: String = ""
    var incorrect_answers: List<String>? = null
    var question: String = ""
    var type: String = ""
}