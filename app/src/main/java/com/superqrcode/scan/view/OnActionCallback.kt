package com.superqrcode.scan.view

interface OnActionCallback {
    fun callback(key: String?, vararg data: Any?)
}