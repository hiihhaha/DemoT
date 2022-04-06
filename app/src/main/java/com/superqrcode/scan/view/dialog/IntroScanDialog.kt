package com.superqrcode.scan.view.dialog

import android.content.Context
import android.content.Intent
import com.docxmaster.docreader.base.BaseActivity
import com.superqrcode.scan.R
import kotlinx.android.synthetic.main.dialog_intro_scan.*

class IntroScanDialog(
    override val layoutId: Int = R.layout.dialog_intro_scan
) : BaseActivity() {
    override fun initView() {

    }

    override fun addEvent() {
        bt_ok.setOnClickListener { finish() }
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, IntroScanDialog::class.java)
            context.startActivity(starter)
        }
    }
}