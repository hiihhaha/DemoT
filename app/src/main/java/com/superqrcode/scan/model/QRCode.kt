package com.superqrcode.scan.model

import com.google.zxing.BarcodeFormat
import com.superqrcode.scan.utils.QRCodeUtils
import java.io.Serializable

class QRCode : Serializable {
    var icon: Int
    var name: String
    var content: String
    var typeCode: Int
    var timeMillis: Long
    var resultOfTypeAndValue: ResultOfTypeAndValue
    var barcodeFormat = BarcodeFormat.CODE_128

    constructor(content: String, typeCode: Int) {
        this.content = content
        resultOfTypeAndValue = QRCodeUtils.getResourceType(content)
        name = QRCodeUtils.getName(resultOfTypeAndValue.type)
        icon = QRCodeUtils.getIcon(resultOfTypeAndValue.type)
        this.typeCode = typeCode
        timeMillis = System.currentTimeMillis()
    }

    constructor(icon: Int, name: String, content: String, typeCode: Int) {
        this.icon = icon
        this.name = name
        this.content = content
        this.typeCode = typeCode
        resultOfTypeAndValue = QRCodeUtils.getResourceType(content)
        timeMillis = System.currentTimeMillis()
    }

    constructor(
        icon: Int,
        name: String,
        content: String,
        typeCode: Int,
        barcodeFormat: BarcodeFormat
    ) {
        this.icon = icon
        this.name = name
        this.content = content
        this.typeCode = typeCode
        resultOfTypeAndValue = QRCodeUtils.getResourceType(content)
        timeMillis = System.currentTimeMillis()
        this.barcodeFormat = barcodeFormat
    }
}