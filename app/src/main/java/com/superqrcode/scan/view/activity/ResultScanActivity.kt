package com.superqrcode.scan.view.activity

import android.content.Context
import android.content.Intent
import com.docxmaster.docreader.base.BaseActivity
import com.superqrcode.scan.R
import com.superqrcode.scan.model.History
import com.superqrcode.scan.model.QRCode
import com.superqrcode.scan.view.fragment.ResultScanFragment

class ResultScanActivity(override val layoutId: Int = R.layout.activity_result_scan) :
    BaseActivity() {
    override fun initView() {
        val qrCode = intent.getSerializableExtra("data") as QRCode
        val history = intent.getSerializableExtra("history") as History?
        if (history == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fr_content, ResultScanFragment.newInstance(qrCode)).commit()
        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.fr_content, ResultScanFragment.newInstance(qrCode, history)).commit()
        }
    }

    override fun addEvent() {
    }

    companion object {
        @JvmStatic
        fun start(context: Context, qrCode: QRCode, history: History?) {
            val starter = Intent(context, ResultScanActivity::class.java)
            starter.putExtra("data", qrCode)
            starter.putExtra("history", history)
            context.startActivity(starter)
        }
    }
}