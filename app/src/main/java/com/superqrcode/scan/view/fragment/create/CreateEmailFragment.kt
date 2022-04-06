package com.superqrcode.scan.view.fragment.create

import android.os.Bundle
import com.docxmaster.docreader.base.BaseFragment
import com.superqrcode.scan.R
import com.superqrcode.scan.extension.addEventEdittext
import com.superqrcode.scan.model.QRCode
import com.superqrcode.scan.utils.CodeGenerator
import com.superqrcode.scan.utils.QRCodeUtils
import com.superqrcode.scan.view.activity.ResultCreateActivity
import kotlinx.android.synthetic.main.fragment_create_email.*

class CreateEmailFragment(override val layoutId: Int = R.layout.fragment_create_email) :
    BaseFragment() {


    override fun initView() {}
    override fun addEvent() {
        edt_email.addEventEdittext(R.drawable.ic_cr_email)
        edt_subject.addEventEdittext(R.drawable.ic_cr_suject_cal)
        create.setOnClickListener {
            createQR()
        }
    }

    private fun createQR() {
        val text = QRCodeUtils.getEmailCode(
            edt_email.text.toString(),
            edt_subject.text.toString(), edt_body.text.toString()

        )
        val qrCode = QRCode(R.drawable.ic_email, "Email", text, CodeGenerator.TYPE_QR);
        openResult(qrCode)
    }

    companion object {
        fun newInstance(): CreateEmailFragment {
            val args = Bundle()
            val fragment = CreateEmailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}