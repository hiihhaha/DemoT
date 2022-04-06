package com.superqrcode.scan.view.fragment.create

import android.os.Bundle
import com.docxmaster.docreader.base.BaseFragment
import com.superqrcode.scan.R

class CreateCalendarFragment(override val layoutId: Int = R.layout.fragment_create_calendar) :
    BaseFragment() {


    override fun initView() {}
    override fun addEvent() {}

    companion object {
        fun newInstance(): CreateCalendarFragment {
            val args = Bundle()
            val fragment = CreateCalendarFragment()
            fragment.arguments = args
            return fragment
        }
    }
}