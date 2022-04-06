package com.superqrcode.scan.view.activity

import android.content.Context
import android.content.Intent
import com.docxmaster.docreader.base.BaseActivity
import com.superqrcode.scan.R

class PreviewImageActivity(override val layoutId: Int = R.layout.activity_preview) :
    BaseActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, PreviewImageActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun initView() {

    }

    override fun addEvent() {
    }
}