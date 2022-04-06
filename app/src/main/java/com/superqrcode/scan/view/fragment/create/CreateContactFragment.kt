package com.superqrcode.scan.view.fragment.create

import android.os.Bundle
import android.text.Editable
import com.docxmaster.docreader.base.BaseFragment
import com.superqrcode.scan.R
import com.superqrcode.scan.extension.addEventEdittext
import com.superqrcode.scan.model.QRCode
import com.superqrcode.scan.utils.CodeGenerator
import com.superqrcode.scan.utils.QRCodeUtils
import com.superqrcode.scan.view.OnCommonCallback
import com.superqrcode.scan.view.activity.ResultCreateActivity
import com.superqrcode.scan.view.widget.CustomEditText
import kotlinx.android.synthetic.main.fragment_create_contact.*

class CreateContactFragment(override val layoutId: Int = R.layout.fragment_create_contact) :
    BaseFragment() {

    override fun initView() {}
    override fun addEvent() {
        edt_name.addEventEdittext(R.drawable.ic_cr_name)
        edt_oran.addEventEdittext(R.drawable.ic_cr_organization)
        edt_add.addEventEdittext(R.drawable.ic_cr_address)
        edt_phone.addEventEdittext(R.drawable.ic_cr_phone)
        edt_email.addEventEdittext(R.drawable.ic_cr_email)
        create.setOnClickListener {
            createQR()
        }
    }

    private fun createQR() {
        val text = QRCodeUtils.getContactCode(
            edt_name.text.toString(),
            edt_oran.text.toString(),
            edt_phone.text.toString(),
            edt_email.text.toString(),
            edt_add.text.toString(),
            edt_note.text.toString()
        );
        val qrCode = QRCode(R.drawable.ic_contact, "Contact", text, CodeGenerator.TYPE_QR)
        openResult(qrCode)
    }

    //

    //    @Override
    //    protected void getTextCode() {
    //        super.getTextCode();
    //        if (callback != null) {
//                String text = QRCodeUtils.getContactCode(binding.edtName.getText().toString(),
//                        binding.edtOran.getText().toString(),
//                        binding.edtPhone.getText().toString(),
//                        binding.edtEmail.getText().toString(),
//                        binding.edtAdd.getText().toString(),
//                        binding.edtNote.getText().toString());
//                QRCode qrCode = new QRCode(R.drawable.ic_contact, "Contact", text, CodeGenerator.TYPE_QR);
    //            callback.callback(MainActivity.K_RESULT_CREATE, qrCode);
    //        }
    //    }
    companion object {
        fun newInstance(): CreateContactFragment {
            val args = Bundle()
            val fragment = CreateContactFragment()
            fragment.arguments = args
            return fragment
        }
    }
}