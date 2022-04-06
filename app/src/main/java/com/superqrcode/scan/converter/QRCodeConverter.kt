package com.superqrcode.scan.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.superqrcode.scan.model.QRCode

object QRCodeConverter {
    @JvmStatic
    @TypeConverter
    fun listToString(data: QRCode?): String {
        return Gson().toJson(data)
    }

    @JvmStatic
    @TypeConverter
    fun stringToList(data: String?): QRCode {
        return Gson().fromJson(data, object : TypeToken<QRCode?>() {}.type)
    }
}