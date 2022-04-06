package com.superqrcode.scan.view.fragment.create

import android.os.Bundle
import com.docxmaster.docreader.base.BaseFragment
import com.superqrcode.scan.R
import com.superqrcode.scan.extension.addEventEdittext
import com.superqrcode.scan.model.QRCode
import com.superqrcode.scan.utils.CodeGenerator
import com.superqrcode.scan.utils.QRCodeUtils
import com.superqrcode.scan.view.activity.ResultCreateActivity
import kotlinx.android.synthetic.main.fragment_create_geo.*

class CreateGeoFragment(override val layoutId: Int = R.layout.fragment_create_geo) :
    BaseFragment() {


    override fun initView() {}
    override fun addEvent() {
        edt_lat.addEventEdittext(R.drawable.ic_cr_latitate)
        edt_long.addEventEdittext(R.drawable.ic_cr_longatite)
        edt_query.addEventEdittext(R.drawable.ic_cr_address_location)

        create.setOnClickListener {
            createQR()
        }
    }

    private fun createQR() {
        val text = QRCodeUtils.getGeoCode(
            edt_lat.text.toString(),
            edt_long.text.toString(),
            edt_query.text.toString()
        )
        val qrCode = QRCode(R.drawable.ic_geo, "Geo", text, CodeGenerator.TYPE_QR)
        openResult(qrCode)
    }

    companion object {
        fun newInstance(): CreateGeoFragment {
            val args = Bundle()
            val fragment = CreateGeoFragment()
            fragment.arguments = args
            return fragment
        }
    }
}