package com.superqrcode.scan.view.fragment.create

import android.os.Bundle
import com.docxmaster.docreader.base.BaseFragment
import com.superqrcode.scan.R
import com.superqrcode.scan.extension.addEventEdittext
import com.superqrcode.scan.model.QRCode
import com.superqrcode.scan.utils.CodeGenerator
import com.superqrcode.scan.utils.QRCodeUtils
import kotlinx.android.synthetic.main.fragment_create_phone.*

class CreatePhoneFragment(override val layoutId: Int = R.layout.fragment_create_phone) :
    BaseFragment() {

    override fun initView() {}
    override fun addEvent() {
        edt_phone.addEventEdittext(R.drawable.ic_cr_phone)
        create.setOnClickListener {
            val text = QRCodeUtils.getPhoneCode(edt_phone.text.toString())
            val qrCode = QRCode(R.drawable.ic_phone, "Phone", text, CodeGenerator.TYPE_QR);
            openResult(qrCode)
        }
    }

    //
    //    @Override
    //    protected void getTextCode() {
    //        super.getTextCode();
    //        if (callback != null) {
    //            String text = QRCodeUtils.getPhoneCode(binding.edtPhone.getText().toString());
    //            QRCode qrCode = new QRCode(R.drawable.ic_phone, "Phone", text, CodeGenerator.TYPE_QR);
    //            callback.callback(MainActivity.K_RESULT_CREATE, qrCode);
    //        }
    //    }

    companion object {
        fun newInstance(): CreatePhoneFragment {
            val args = Bundle()
            val fragment = CreatePhoneFragment()
            fragment.arguments = args
            return fragment
        }
    }
}