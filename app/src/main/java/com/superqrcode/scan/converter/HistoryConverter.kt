package com.superqrcode.scan.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.superqrcode.scan.model.History

object HistoryConverter {
    @TypeConverter
    fun listToString(data: History?): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun stringToList(data: String?): History {
        return Gson().fromJson(data, object : TypeToken<History?>() {}.type)
    }
}