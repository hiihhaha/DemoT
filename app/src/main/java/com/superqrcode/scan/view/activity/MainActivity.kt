package com.superqrcode.scan.view.activity

import android.content.Intent
import com.docxmaster.docreader.base.BaseActivity
import com.superqrcode.scan.R
import com.superqrcode.scan.view.adapter.CustomFragmentPagerAdapter
import com.superqrcode.scan.view.fragment.CreateQRFragment
import com.superqrcode.scan.view.fragment.HistoryFragment
import com.superqrcode.scan.view.fragment.ScanFragment
import com.superqrcode.scan.view.fragment.SettingFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity(override val layoutId: Int = R.layout.activity_main) : BaseActivity() {
    override fun initView() {
        initViewPager()

    }

    companion object {
        var pauseScan: Boolean = false
    }

    private fun initViewPager() {
        val adapter = CustomFragmentPagerAdapter(supportFragmentManager)
        adapter.addFragment(ScanFragment.newInstance())
        adapter.addFragment(HistoryFragment.newInstance())
        adapter.addFragment(CreateQRFragment.newInstance())
        adapter.addFragment(SettingFragment.newInstance())
        view_main.offscreenPageLimit = 4
        view_main.setPagingEnabled(false)
        view_main.adapter = adapter
    }

    override fun addEvent() {
        bottom_navigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_scan -> {
                    view_main.currentItem = 0
                    pauseScan = false
                    sendBroadcast(Intent("ACTION_CAMERA"))
                }
                R.id.nav_history -> {
                    view_main.currentItem = 1
                    pauseScan = true
                    sendBroadcast(Intent("ACTION_CAMERA"))
                }
                R.id.nav_create -> {
                    view_main.currentItem = 2
                    pauseScan = true
                    sendBroadcast(Intent("ACTION_CAMERA"))
                }
                R.id.nav_setting -> {
                    view_main.currentItem = 3
                    pauseScan = true
                    sendBroadcast(Intent("ACTION_CAMERA"))
                }
            }
            true
        }
        bottom_navigation.setOnItemReselectedListener { }
    }

    override fun onBackPressed() {
        if (view_main.currentItem != 0) {
            view_main.currentItem = 0
            pauseScan = false
            sendBroadcast(Intent("ACTION_CAMERA"))
            bottom_navigation.selectedItemId = R.id.nav_scan
            return
        }
        finish()
    }
}