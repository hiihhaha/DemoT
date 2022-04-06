package com.superqrcode.scan.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.common.control.utils.LanguageUtils
import com.docxmaster.docreader.base.BaseFragment
import com.superqrcode.scan.R
import com.superqrcode.scan.utils.CommonUtils
import com.superqrcode.scan.utils.SharePreferenceUtils
import com.superqrcode.scan.view.activity.LanguageActivity
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment(
    override val layoutId: Int = R.layout.fragment_setting
) :
    BaseFragment() {


    override fun initView() {
        tv_lang.text = LanguageUtils.getCurrentLanguage(context).name
        cb_copy.isChecked = SharePreferenceUtils.isCopy(context)
        cb_focus.isChecked = SharePreferenceUtils.isFocus(context)
        cb_beep.isChecked = SharePreferenceUtils.isBeep(context)
        cb_open_url_auto.isChecked = SharePreferenceUtils.isOpenUrlAuto(context)
    }

    override fun addEvent() {
        ll_beep.setOnClickListener { v ->
            SharePreferenceUtils.setBeep(context, !cb_beep.isChecked)
            cb_beep.isChecked = !cb_beep.isChecked
        }
        ll_focus.setOnClickListener { v ->
            SharePreferenceUtils.setFocus(context, !cb_focus.isChecked)
            cb_focus.isChecked = !cb_focus.isChecked
        }
        ll_copy.setOnClickListener { v ->
            SharePreferenceUtils.setCopy(context, !cb_copy.isChecked)
            cb_copy.isChecked = !cb_copy.isChecked
        }
        ll_open_url_auto.setOnClickListener { v ->
            SharePreferenceUtils.setOpenUrlAuto(context, !cb_open_url_auto.isChecked)
            cb_open_url_auto.isChecked = !cb_open_url_auto.isChecked
        }
        ll_language.setOnClickListener { v ->
            startActivity(
                Intent(
                    context,
                    LanguageActivity::class.java
                )
            )
        }
        bt_share.setOnClickListener {
            CommonUtils.getInstance().shareApp(context)
        }
        bt_policy.setOnClickListener {
            CommonUtils.getInstance().showPolicy(context)
        }

    }

    companion object {
        fun newInstance(): SettingFragment {
            val args = Bundle()
            val fragment = SettingFragment()
            fragment.arguments = args
            return fragment
        }
    }
}