package com.docxmaster.docreader.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.superqrcode.scan.model.QRCode
import com.superqrcode.scan.view.activity.ResultCreateActivity

abstract class BaseFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        addEvent()
    }

    protected abstract val layoutId: Int
    protected abstract fun initView()
    protected abstract fun addEvent()

    override fun onClick(v: View) {}


    protected fun openResult(qrCode: QRCode) {
        context?.let { ResultCreateActivity.start(it, qrCode) }
        (context as Activity).finish()
    }
}
