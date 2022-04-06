package com.superqrcode.scan.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.superqrcode.scan.converter.QRCodeConverter
import com.google.zxing.BarcodeFormat
import com.superqrcode.scan.model.ResultOfTypeAndValue
import com.superqrcode.scan.utils.QRCodeUtils

@Entity(tableName = "favourite_table")
class Favourite(var historyId: Int) {
    @PrimaryKey(autoGenerate = true)
    var id = 0

}