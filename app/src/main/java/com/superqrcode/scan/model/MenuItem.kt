package com.superqrcode.scan.model

import com.docxmaster.docreader.base.BaseFragment
import com.google.zxing.BarcodeFormat
import java.io.Serializable

class MenuItem : Serializable {
    var icon: Int
    var name: String
    private var fragment: BaseFragment? = null
    var option = 0
        private set
    var type: String? = null
        private set
    var isSelected = false
    var barcodeFormat = BarcodeFormat.CODE_128
        private set

    constructor(icon: Int, name: String, type: String?) {
        this.icon = icon
        this.name = name
        this.type = type
    }

    constructor(icon: Int, name: String, fragment: BaseFragment?) {
        this.icon = icon
        this.name = name
        this.fragment = fragment
    }

    constructor(icon: Int, name: String, fragment: BaseFragment?, option: Int) {
        this.icon = icon
        this.name = name
        this.fragment = fragment
        this.option = option
    }

    constructor(icon: Int, name: String, type: String?, barcodeFormat: BarcodeFormat) {
        this.icon = icon
        this.name = name
        this.type = type
        this.barcodeFormat = barcodeFormat
    }

    constructor(icon: Int, name: String, fragment: BaseFragment?, isSelected: Boolean) {
        this.fragment = fragment
        this.icon = icon
        this.name = name
        this.isSelected = isSelected
    }

    fun getFragment(): BaseFragment? {
        return fragment
    }

    fun setFragment(fragment: BaseFragment?) {
        this.fragment = fragment
    }
}