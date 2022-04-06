package com.superqrcode.scan

interface Type {
    companion object {
        const val T_URL = "t_url"
        const val T_CONTACT = "t_contact"
        const val T_EMAIL = "t_email"
        const val T_CALENDAR = "t_calendar"
        const val T_GEO = "t_geo"
        const val T_PHONE = "t_phone"
        const val T_SMS = "t_sms"
        const val T_TEXT = "t_text"
        const val T_WIFI = "t_wifi"
        const val T_BARCODE = "t_barcode"
        const val T_MY_QR = "t_my_qr"
    }
}