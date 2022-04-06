package com.superqrcode.scan.view.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.docxmaster.docreader.base.BaseFragment
import com.google.zxing.BarcodeFormat
import com.superqrcode.scan.Const
import com.superqrcode.scan.R
import com.superqrcode.scan.Type.Companion.T_BARCODE
import com.superqrcode.scan.Type.Companion.T_CONTACT
import com.superqrcode.scan.Type.Companion.T_EMAIL
import com.superqrcode.scan.Type.Companion.T_GEO
import com.superqrcode.scan.Type.Companion.T_PHONE
import com.superqrcode.scan.Type.Companion.T_SMS
import com.superqrcode.scan.Type.Companion.T_TEXT
import com.superqrcode.scan.Type.Companion.T_URL
import com.superqrcode.scan.Type.Companion.T_WIFI
import com.superqrcode.scan.model.MenuItem
import com.superqrcode.scan.view.OnActionCallback
import com.superqrcode.scan.view.activity.CreateQRActivity
import com.superqrcode.scan.view.adapter.MenuItemAdapter
import kotlinx.android.synthetic.main.fragment_create_qr.*
import java.util.*

open class CreateQRFragment(override val layoutId: Int = R.layout.fragment_create_qr) :
    BaseFragment(), OnActionCallback {


    override fun initView() {
        initQRCodeList()
        initBarCodeList()
    }

    private fun initBarCodeList() {
        val list = Arrays.asList(
            MenuItem(R.drawable.ic_barcode, "EAN_8", T_BARCODE, BarcodeFormat.EAN_8),
            MenuItem(R.drawable.ic_barcode, "EAN_13", T_BARCODE, BarcodeFormat.EAN_13),
            MenuItem(R.drawable.ic_barcode, "UPC_E", T_BARCODE, BarcodeFormat.UPC_E),
            MenuItem(R.drawable.ic_barcode, "UPC_A", T_BARCODE, BarcodeFormat.UPC_A),
            MenuItem(R.drawable.ic_barcode, "CODE_39", T_BARCODE, BarcodeFormat.CODE_39),
            MenuItem(R.drawable.ic_barcode, "CODE_128", T_BARCODE, BarcodeFormat.CODE_128),
            MenuItem(R.drawable.ic_barcode, "ITF", T_BARCODE, BarcodeFormat.ITF),
            MenuItem(R.drawable.ic_barcode, "PDF_417", T_BARCODE, BarcodeFormat.PDF_417),
            MenuItem(R.drawable.ic_barcode, "CODABAR", T_BARCODE, BarcodeFormat.CODE_93),
            MenuItem(R.drawable.ic_barcode, "DATA_MATRIX", T_BARCODE, BarcodeFormat.DATA_MATRIX),
            MenuItem(R.drawable.ic_barcode, "AZTEC", T_BARCODE, BarcodeFormat.AZTEC)
        )
        val adapter = MenuItemAdapter(list, context)

        adapter.mCallback = this
        rv_barcode.layoutManager = LinearLayoutManager(context)
        rv_barcode.adapter = adapter
    }

    private fun initQRCodeList() {
        val list = Arrays.asList(
            MenuItem(R.drawable.ic_url, "URL", T_URL),
            MenuItem(R.drawable.ic_text, "Text", T_TEXT),
            MenuItem(R.drawable.ic_contact, "Contact", T_CONTACT),
            MenuItem(R.drawable.ic_email, "Email", T_EMAIL),
            MenuItem(R.drawable.ic_sms, "SMS", T_SMS),
            MenuItem(R.drawable.ic_geo, "Location", T_GEO),
            MenuItem(
                R.drawable.ic_phone,
                "Phone number",
                T_PHONE
            ),
            MenuItem(
                R.drawable.ic_wifi,
                "Wifi",
                T_WIFI
            ),
//            MenuItem(R.drawable.ic_barcode, "Barcode", T_BARCODE)
        )
        val adapter = MenuItemAdapter(list, context)
        adapter.mCallback = this
        rv_qrcode.layoutManager = LinearLayoutManager(context)
        rv_qrcode.adapter = adapter
    }

    override fun addEvent() {}

    companion object {
        fun newInstance(): CreateQRFragment {
            val args = Bundle()

            val fragment = CreateQRFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun callback(key: String?, vararg data: Any?) {
        if (Const.K_CLICK_ITEM == key) {
            val menuItem = data.get(0) as MenuItem
            context?.let { CreateQRActivity.start(it, menuItem) }
        }
    }
}