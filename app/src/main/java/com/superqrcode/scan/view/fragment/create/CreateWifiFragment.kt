package com.superqrcode.scan.view.fragment.create

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.docxmaster.docreader.base.BaseFragment
import com.superqrcode.scan.R
import com.superqrcode.scan.extension.addEventEdittext
import com.superqrcode.scan.model.QRCode
import com.superqrcode.scan.utils.CodeGenerator
import com.superqrcode.scan.utils.QRCodeUtils
import kotlinx.android.synthetic.main.fragment_create_wifi.*

class CreateWifiFragment(override val layoutId: Int = R.layout.fragment_create_wifi) :
    BaseFragment() {


    override fun initView() {}
    override fun addEvent() {
        edt_ssid.addEventEdittext(R.drawable.ic_cr_wifi)
        edt_password.addEventEdittext(R.drawable.ic_cr_password)
        iv_dropdown.setOnClickListener { v: View -> showPopup(v) }

        create.setOnClickListener {
            val typeWifi: String = tv_wifi_type.text.toString()
            val text = QRCodeUtils.getWifiCode(
                edt_ssid.text.toString(),
                if (typeWifi.contains("None")) "" else edt_password.text.toString(),
                if (typeWifi.contains("None")) "nopass" else typeWifi,
                cb_hidden.isChecked
            )
            val qrCode = QRCode(R.drawable.ic_wifi, "Wifi", text, CodeGenerator.TYPE_QR)
            openResult(qrCode)
        }
    }

    private fun showPopup(v: View) {
        val popup = context?.let { PopupMenu(it, v) }
        popup?.inflate(R.menu.popup_wifi)
        val menu = popup?.menu
        popup?.setOnMenuItemClickListener { item: MenuItem ->
            if (item.itemId == R.id.wpa) {
                tv_wifi_type.setText("WPA/WPAS")
            } else if (item.itemId == R.id.wep) {
                tv_wifi_type.setText("WEP")
            } else {
                tv_wifi_type.setText("None")
            }
            false
        }

        // Show the PopupMenu.
        popup?.show()
    }

//    protected val textCode: Unit
//        protected get() {
//            super.getTextCode()
//            if (callback != null) {
//                val typeWifi: String = binding.tvWifiType.getText().toString()
//                val text = QRCodeUtils.getWifiCode(
//                    binding.edtSsid.getText().toString(),
//                    if (typeWifi.contains("None")) "" else binding.edtPassword.getText().toString(),
//                    if (typeWifi.contains("None")) "nopass" else typeWifi,
//                    binding.cbHidden.isChecked()
//                )
//                val qrCode = QRCode(R.drawable.ic_wifi, "Wifi", text, CodeGenerator.TYPE_QR)
//                callback.callback(MainActivity.K_RESULT_CREATE, qrCode)
//            }
//        }

    companion object {
        fun newInstance(): CreateWifiFragment {
            val args = Bundle()
            val fragment = CreateWifiFragment()
            fragment.arguments = args
            return fragment
        }
    }
}