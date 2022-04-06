package com.superqrcode.scan.view.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.text.SpannableString
import android.text.style.UnderlineSpan
import com.bumptech.glide.Glide
import com.common.control.interfaces.PermissionCallback
import com.common.control.manager.AdmobManager
import com.common.control.utils.PermissionUtils
import com.docxmaster.docreader.base.BaseActivity
import com.google.zxing.BarcodeFormat
import com.superqrcode.scan.App
import com.superqrcode.scan.BuildConfig
import com.superqrcode.scan.R
import com.superqrcode.scan.Type
import com.superqrcode.scan.Type.Companion.T_URL
import com.superqrcode.scan.model.Favourite
import com.superqrcode.scan.model.History
import com.superqrcode.scan.model.QRCode
import com.superqrcode.scan.model.ResultOfTypeAndValue
import com.superqrcode.scan.utils.CodeGenerator
import com.superqrcode.scan.utils.CommonUtils
import com.superqrcode.scan.utils.QRCodeUtils
import com.superqrcode.scan.view.OnActionCallback
import kotlinx.android.synthetic.main.activity_result_create.*
import kotlinx.android.synthetic.main.activity_result_create.bt_back
import kotlinx.android.synthetic.main.activity_result_create.iv_qrcode
import kotlinx.android.synthetic.main.activity_result_create.tv_content
import kotlinx.android.synthetic.main.activity_result_create.tv_date
import kotlinx.android.synthetic.main.activity_result_create.tv_title
import kotlinx.android.synthetic.main.fragment_result_scan.*

class ResultCreateActivity(
    override val layoutId: Int = R.layout.activity_result_create
) : BaseActivity(), OnActionCallback {
    private var history: History? = null
    private var qrCode: QRCode? = null
    private var bitmap: Bitmap? = null

    override fun initView() {
        AdmobManager.getInstance().loadBanner(this, BuildConfig.banner_result_create)
        qrCode = intent.getSerializableExtra("data") as QRCode
        history = History(qrCode!!, System.currentTimeMillis())
//        App.getInstance().database.historyDao().add(history)
        if (qrCode?.typeCode == CodeGenerator.TYPE_BAR) {
            QRCodeUtils.createBarCode(
                qrCode?.content,
                qrCode?.barcodeFormat,
                this
            )
        } else {
            QRCodeUtils.createQRCode(
                qrCode?.content,
                this
            )
        }
        tv_content.text = qrCode?.content
        tv_title.text = qrCode?.name
        Glide.with(this).load(qrCode?.icon).into(iv_type)
        tv_date.text = CommonUtils.getInstance().formatDate(System.currentTimeMillis())

        if (qrCode?.resultOfTypeAndValue?.type == T_URL) {
            val content = SpannableString(qrCode!!.resultOfTypeAndValue.value)
            content.setSpan(UnderlineSpan(), 0, content.length, 0)
            tv_content.text = content
            tv_content.setTextColor(Color.BLUE)
            tv_content.setOnClickListener {
                QRCodeUtils.openUrl(this, qrCode!!.resultOfTypeAndValue.value)
            }
        }

    }

    private var isFavourite: Boolean = false
    override fun addEvent() {
        bt_back.setOnClickListener {
            finish()
        }
        bt_favourite.setOnClickListener {
            if (!isFavourite) {
                App.getInstance().database.favoriteDao()
                    .add(history?.id?.let { it1 -> Favourite(it1) })
                Glide.with(this).load(R.drawable.ic_favourite_actived).into(bt_favourite)
            } else {
                history?.id?.let { it1 -> App.getInstance().database.favoriteDao().delete(it1) }
                Glide.with(this).load(R.drawable.ic_favourite).into(bt_favourite)
            }
            isFavourite = !isFavourite
        }
        bt_save.setOnClickListener {
            PermissionUtils.instance().showPermissionNormalDialog(
                this,
                object : PermissionCallback {
                    override fun onPermissionNotGranted() {

                    }

                    override fun onPermissionGranted() {
                        bitmap?.let { it1 -> saveBitmap(it1) }
                    }

                    override fun onPermissionDenied() {
                    }

                    override fun onPermissionAborted() {
                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
        bt_share.setOnClickListener {
            bitmap?.let { it1 -> shareQR(it1) }
//            PermissionUtils.instance().showPermissionNormalDialog(
//                this,
//                object : PermissionCallback {
//                    override fun onPermissionNotGranted() {
//
//                    }
//
//                    override fun onPermissionGranted() {
//                        bitmap?.let { it1 -> shareQR(it1) }
//                    }
//
//                    override fun onPermissionDenied() {
//                    }
//
//                    override fun onPermissionAborted() {
//                    }
//                },
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            )

        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context, qrCode: QRCode) {
            val starter = Intent(context, ResultCreateActivity::class.java)
                .putExtra("data", qrCode)
            context.startActivity(starter)
        }
    }

    override fun callback(key: String?, vararg data: Any?) {
        bitmap = data[0] as Bitmap
        bitmap?.let {
            Glide.with(this).load(it).into(iv_qrcode)
        }

    }
}