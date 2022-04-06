package com.superqrcode.scan.view.fragment.create

import com.docxmaster.docreader.base.BaseFragment
import com.superqrcode.scan.R
import android.os.Bundle
import com.google.zxing.BarcodeFormat
import com.superqrcode.scan.extension.addEventEdittext
import com.superqrcode.scan.model.QRCode
import com.superqrcode.scan.utils.CodeGenerator
import com.superqrcode.scan.view.activity.ResultCreateActivity
import kotlinx.android.synthetic.main.frament_create_barcode.*

class CreateBarcodeFragment(override val layoutId: Int = R.layout.frament_create_barcode) :
    BaseFragment() {


    private var barcodeFormat: BarcodeFormat? = null

    override fun initView() {
        barcodeFormat = arguments?.getSerializable("data") as BarcodeFormat
    }

    override fun addEvent() {
        edt_barcode.addEventEdittext(R.drawable.ic_cr_barcode)
        create.setOnClickListener {
            createQR()
        }
    }

    private fun createQR() {
        val qrCode = QRCode(
            R.drawable.ic_barcode,
            "Barcode",
            edt_barcode.text.toString(),
            CodeGenerator.TYPE_BAR,
            barcodeFormat!!
        )
        openResult(qrCode)
    }

    //

    //    @Override
    //    protected void getTextCode() {
    //        super.getTextCode();
    //        if (callback != null && !binding.edtBarcode.getText().toString().trim().isEmpty()) {
    //            String text = binding.edtBarcode.getText().toString();
    //            QRCode qrCode = new QRCode(R.drawable.ic_barcode, "Barcode", text, CodeGenerator.TYPE_BAR);
    //            callback.callback(MainActivity.K_RESULT_CREATE, qrCode);
    //        } else {
    //            binding.edtBarcode.setError("Not empty");
    //        }
    //    }
    companion object {
        fun newInstance(barcodeFormat: BarcodeFormat): CreateBarcodeFragment {
            val args = Bundle()
            args.putSerializable("data", barcodeFormat)
            val fragment = CreateBarcodeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}