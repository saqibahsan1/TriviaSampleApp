package com.saqib.triviasample.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromMessageHistory(msgHistory: List<String?>?): String? {
        val type = object : TypeToken<List<String?>?>() {}.type
        return Gson().toJson(msgHistory, type)
    }

    @TypeConverter
    fun toMessages(msgs: String?): List<String?>? {
        val type = object : TypeToken<List<String?>?>() {}.type
        return Gson().fromJson<List<String>>(msgs, type)
    }

}