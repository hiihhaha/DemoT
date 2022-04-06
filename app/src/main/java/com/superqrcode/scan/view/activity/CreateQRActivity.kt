package com.superqrcode.scan.view.activity

import android.content.Context
import android.content.Intent
import com.docxmaster.docreader.base.BaseActivity
import com.docxmaster.docreader.base.BaseFragment
import com.superqrcode.scan.R
import com.superqrcode.scan.Type
import com.superqrcode.scan.model.MenuItem
import com.superqrcode.scan.view.fragment.create.*
import kotlinx.android.synthetic.main.activity_create_qr.*

class CreateQRActivity(override val layoutId: Int = R.layout.activity_create_qr) : BaseActivity() {
    override fun initView() {
        val menuItem = intent.getSerializableExtra("data") as MenuItem
        loadPage(menuItem)
    }

    private fun loadPage(menuItem: MenuItem) {
        tv_title.text = menuItem.name

        when (menuItem.type) {
            Type.T_BARCODE -> {
                addFragment(CreateBarcodeFragment.newInstance(menuItem.barcodeFormat))
            }
            Type.T_EMAIL -> {
                addFragment(CreateEmailFragment.newInstance())
            }
            Type.T_CALENDAR -> {
                addFragment(CreateCalendarFragment.newInstance())
            }
            Type.T_TEXT -> {
                addFragment(CreateTextFragment.newInstance())
            }
            Type.T_CONTACT -> {
                addFragment(CreateContactFragment.newInstance())
            }
            Type.T_GEO -> {
                addFragment(CreateGeoFragment.newInstance())
            }
            Type.T_SMS -> {
                addFragment(CreateSmsFragment.newInstance())
            }
            Type.T_URL -> {
                addFragment(CreateUrlFragment.newInstance())
            }
            Type.T_WIFI -> {
                addFragment(CreateWifiFragment.newInstance())
            }
            Type.T_PHONE -> {
                addFragment(CreatePhoneFragment.newInstance())
            }
        }

    }

    private fun addFragment(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction().add(R.id.fr_content, fragment).commit()
    }

    override fun addEvent() {
        bt_back.setOnClickListener {
            finish()
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context, menuItem: MenuItem) {
            val starter = Intent(context, CreateQRActivity::class.java)
                .putExtra("data", menuItem)
            context.startActivity(starter)
        }
    }
}