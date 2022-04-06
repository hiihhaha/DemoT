package com.superqrcode.scan.view.fragment.create

import android.os.Bundle
import com.docxmaster.docreader.base.BaseFragment
import com.superqrcode.scan.R
import com.superqrcode.scan.extension.addEventEdittext
import com.superqrcode.scan.model.QRCode
import com.superqrcode.scan.utils.CodeGenerator
import com.superqrcode.scan.utils.QRCodeUtils
import kotlinx.android.synthetic.main.fragment_create_url.*

class CreateUrlFragment(override val layoutId: Int = R.layout.fragment_create_url) :
    BaseFragment() {


    override fun initView() {}
    override fun addEvent() {
        edt_url.addEventEdittext(R.drawable.ic_cr_url)
        create.setOnClickListener {
            val text = QRCodeUtils.getURLCode(edt_url.text.toString())
            val qrCode = QRCode(R.drawable.ic_url, "URL", text, CodeGenerator.TYPE_QR);
            openResult(qrCode)
        }
    }
    //    @Override

    //    protected void getTextCode() {
    //        super.getTextCode();
    //        if (callback != null) {
    //            String text = QRCodeUtils.getURLCode(binding.edtUrl.getText().toString());
    //            QRCode qrCode = new QRCode(R.drawable.ic_url, "URL", text, CodeGenerator.TYPE_QR);
    //            callback.callback(MainActivity.K_RESULT_CREATE, qrCode);
    //        }
    //    }
    companion object {
        fun newInstance(): CreateUrlFragment {
            val args = Bundle()
            val fragment = CreateUrlFragment()
            fragment.arguments = args
            return fragment
        }
    }
}