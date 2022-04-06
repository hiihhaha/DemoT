package com.superqrcode.scan.view.fragment.create

import com.docxmaster.docreader.base.BaseFragment
import com.superqrcode.scan.R
import com.superqrcode.scan.view.fragment.create.CreateGeoFragment
import android.os.Bundle
import com.superqrcode.scan.extension.addEventEdittext
import com.superqrcode.scan.model.QRCode
import com.superqrcode.scan.utils.CodeGenerator
import com.superqrcode.scan.view.fragment.create.CreateSmsFragment
import com.superqrcode.scan.view.fragment.create.CreateTextFragment
import com.superqrcode.scan.view.fragment.create.CreateUrlFragment
import com.superqrcode.scan.utils.QRCodeUtils
import com.superqrcode.scan.view.activity.ResultCreateActivity
import com.superqrcode.scan.view.fragment.create.CreateWifiFragment
import kotlinx.android.synthetic.main.fragment_create_sms.*

class CreateSmsFragment(override val layoutId: Int = R.layout.fragment_create_sms) :
    BaseFragment() {


    override fun initView() {}
    override fun addEvent() {
        edt_phone.addEventEdittext(R.drawable.ic_cr_phone)
        create.setOnClickListener {
            val text = QRCodeUtils.getSMSCode(
                edt_phone.text.toString(),
                edt_message.text.toString()
            );
            val qrCode = QRCode(R.drawable.ic_sms, "SMS", text, CodeGenerator.TYPE_QR)
            openResult(qrCode)
        }
    }

    companion object {
        fun newInstance(): CreateSmsFragment {
            val args = Bundle()
            val fragment = CreateSmsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}