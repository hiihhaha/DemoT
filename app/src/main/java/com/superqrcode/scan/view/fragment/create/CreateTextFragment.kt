package com.superqrcode.scan.view.fragment.create

import android.os.Bundle
import com.docxmaster.docreader.base.BaseFragment
import com.superqrcode.scan.R
import com.superqrcode.scan.model.QRCode
import com.superqrcode.scan.utils.CodeGenerator
import kotlinx.android.synthetic.main.fragment_create_text.*

class CreateTextFragment(override val layoutId: Int = R.layout.fragment_create_text) :
    BaseFragment() {


    override fun initView() {}
    override fun addEvent() {
        create.setOnClickListener {
            val text = edt_text.text.toString()
            val qrCode = QRCode(R.drawable.ic_text, "Text", text, CodeGenerator.TYPE_QR);
            openResult(qrCode)
        }
    }

    //    @Override
    //    protected void getTextCode() {
    //        super.getTextCode();
    //        if (callback != null) {
    //            String text = binding.edtText.getText().toString().trim();
    //            if (TextUtils.isEmpty(text)) {
    //                binding.edtText.setError("Not empty this field");
    //                return;
    //            }
    //            QRCode qrCode = new QRCode(R.drawable.ic_text, "Text", text, CodeGenerator.TYPE_QR);
    //            callback.callback(MainActivity.K_RESULT_CREATE, qrCode);
    //        }
    //    }
    companion object {
        fun newInstance(): CreateTextFragment {
            val args = Bundle()
            val fragment = CreateTextFragment()
            fragment.arguments = args
            return fragment
        }
    }
}