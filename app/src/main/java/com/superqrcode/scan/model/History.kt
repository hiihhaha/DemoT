package com.superqrcode.scan.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.superqrcode.scan.converter.QRCodeConverter
import com.google.zxing.BarcodeFormat
import com.superqrcode.scan.model.ResultOfTypeAndValue
import com.superqrcode.scan.utils.QRCodeUtils
import java.io.Serializable

@Entity(tableName = "history_table")
class History(
    @field:TypeConverters(QRCodeConverter::class) var qrCode: QRCode,
    var currentTime: Long
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    var isFavourite = false

}