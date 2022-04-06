package com.superqrcode.scan.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import com.common.control.manager.AdmobManager
import com.docxmaster.docreader.base.BaseFragment
import com.superqrcode.scan.App
import com.superqrcode.scan.BuildConfig
import com.superqrcode.scan.Const
import com.superqrcode.scan.R
import com.superqrcode.scan.Type.Companion.T_CONTACT
import com.superqrcode.scan.Type.Companion.T_EMAIL
import com.superqrcode.scan.Type.Companion.T_PHONE
import com.superqrcode.scan.Type.Companion.T_SMS
import com.superqrcode.scan.Type.Companion.T_URL
import com.superqrcode.scan.Type.Companion.T_WIFI
import com.superqrcode.scan.model.Favourite
import com.superqrcode.scan.model.History
import com.superqrcode.scan.model.QRCode
import com.superqrcode.scan.utils.CodeGenerator
import com.superqrcode.scan.utils.CommonUtils
import com.superqrcode.scan.utils.QRCodeUtils
import com.superqrcode.scan.utils.SharePreferenceUtils
import com.superqrcode.scan.view.OnActionCallback
import kotlinx.android.synthetic.main.fragment_result_scan.*
import kotlinx.android.synthetic.main.layout_option.*
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ResultScanFragment(override val layoutId: Int = R.layout.fragment_result_scan) :
    BaseFragment() {
    private var qrCode: QRCode? = null
    private var history: History? = null
    private var isFavourite = false
    private var bitmap: Bitmap? = null

    @SuppressLint("SetTextI18n")
    override fun initView() {
        AdmobManager.getInstance().loadNative(context, BuildConfig.native_result, fr_ads)
        qrCode = arguments?.getSerializable("data") as QRCode
        history = arguments?.getSerializable("history") as History?
        if (history == null) {
            history = History(qrCode!!, System.currentTimeMillis())
            App.getInstance().database.historyDao().add(history)
            history!!.id = App.getInstance().database.historyDao().list.size
        } else {
            isFavourite = history!!.isFavourite
            qrCode = history!!.qrCode
        }
        iv_favorite.setImageResource(if (isFavourite) R.drawable.ic_favourite_actived else R.drawable.ic_favourite)
        tv_content.text = qrCode!!.resultOfTypeAndValue.value
        if (qrCode?.typeCode == CodeGenerator.TYPE_BAR) {
            QRCodeUtils.createBarCode(
                qrCode!!.content, qrCode?.barcodeFormat, object :
                    OnActionCallback {
                    override fun callback(key: String?, vararg data: Any?) {
                        bitmap = data[0] as Bitmap?
                        iv_qrcode.setImageBitmap(bitmap)
                    }

                })
            iv_icon.setImageResource(R.drawable.ic_barcode)
        } else {
            QRCodeUtils.createQRCode(
                qrCode!!.content, object :
                    OnActionCallback {
                    override fun callback(key: String?, vararg data: Any?) {
                        bitmap = data[0] as Bitmap?
                        iv_qrcode.setImageBitmap(bitmap)
                    }

                })
            iv_icon.setImageResource(QRCodeUtils.getIcon(qrCode!!.resultOfTypeAndValue.type))
        }


        tv_name.text = qrCode!!.name
        tv_date.text = (CommonUtils.getInstance().formatDate(
            qrCode!!.timeMillis
        ))
        checkType()
        if (!hasAd && SharePreferenceUtils.isOpenUrlAuto(context)) {
            QRCodeUtils.openUrl(context as Activity, qrCode?.content)
        }
    }

    private fun checkType() {
        if (qrCode!!.resultOfTypeAndValue.type == T_URL) {
            bt_open_url.visibility = View.VISIBLE
            val content = SpannableString(qrCode!!.resultOfTypeAndValue.value)
            content.setSpan(UnderlineSpan(), 0, content.length, 0)
            tv_content.text = content
            tv_content.setTextColor(Color.BLUE)
            tv_content.setOnClickListener {
                QRCodeUtils.openUrl(context as Activity, qrCode!!.resultOfTypeAndValue.value)
            }
            return
        }
        if (qrCode!!.resultOfTypeAndValue.type == T_CONTACT) {
            bt_add_contact.visibility = View.VISIBLE
            bt_call.visibility = View.VISIBLE
            return
        }
        if (qrCode!!.resultOfTypeAndValue.type == T_EMAIL) {
            bt_send_email.visibility = View.VISIBLE
            return
        }
        if (qrCode!!.resultOfTypeAndValue.type == T_SMS) {
            bt_send_sms.visibility = View.VISIBLE
            bt_call.visibility = View.VISIBLE
            return
        }
        if (qrCode!!.resultOfTypeAndValue.type == T_WIFI) {
            bt_connect_wifi.visibility = View.VISIBLE
            return
        }
        if (qrCode!!.resultOfTypeAndValue.type == T_PHONE) {
            bt_call.visibility = View.VISIBLE
            return
        }
        bt_search_web.visibility = View.VISIBLE
    }

    @SuppressLint("DefaultLocale")
    override fun addEvent() {
        bt_back.setOnClickListener {
            (context as Activity).finish()
        }
        iv_favorite.setOnClickListener {
            if (!isFavourite) {
                App.getInstance().database.favoriteDao().add(Favourite(history!!.id))
                Toast.makeText(context, getString(R.string.add_to_fav), Toast.LENGTH_SHORT).show()
            } else {
                App.getInstance().database.favoriteDao().delete(history!!.id)
                Toast.makeText(context, getString(R.string.removed_fav), Toast.LENGTH_SHORT).show()
            }
            isFavourite = !isFavourite
            iv_favorite.setImageResource(if (isFavourite) R.drawable.ic_favourite_actived else R.drawable.ic_favourite)
        }
//        bt_save.setOnClickListener {
//            run {
//                run {
//                    PermissionUtils.instance().showPermissionNormalDialog(
//                        context as Activity,
//                        object : PermissionCallback {
//                            override fun onPermissionNotGranted() {
//
//                            }
//
//                            override fun onPermissionGranted() {
//                                bitmap?.let { it1 -> saveBitmap(it1) }
//                            }
//
//                            override fun onPermissionDenied() {
//                            }
//
//                            override fun onPermissionAborted() {
//                            }
//                        },
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                    )
//                }
//            }
//        }
        bt_copy.setOnClickListener {
            QRCodeUtils.copyToClipboard(context, qrCode?.resultOfTypeAndValue?.value)
            Toast.makeText(context, getString(R.string.copy_in_boardcast), Toast.LENGTH_SHORT)
                .show()
        }
        bt_share.setOnClickListener {
            bitmap?.let { it1 -> shareQR(it1) }
//            run {
//                PermissionUtils.instance().showPermissionNormalDialog(
//                    context as Activity,
//                    object : PermissionCallback {
//                        override fun onPermissionNotGranted() {
//
//                        }
//
//                        override fun onPermissionGranted() {
//                            bitmap?.let { it1 -> shareQR(it1) }
//                        }
//
//                        override fun onPermissionDenied() {
//                        }
//
//                        override fun onPermissionAborted() {
//                        }
//                    },
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                )
//            }
        }
        bt_send_sms.setOnClickListener {
            QRCodeUtils.sendMessage(
                activity,
                qrCode!!.content
            )
        }
        bt_send_email.setOnClickListener {
            QRCodeUtils.sendEmail(
                activity,
                qrCode!!.content
            )
        }
        bt_connect_wifi.setOnClickListener {
            QRCodeUtils.connectWifi(
                activity,
                qrCode!!.content
            )
        }
        bt_search_web.setOnClickListener {
            QRCodeUtils.searchInWeb(
                activity,
                qrCode!!.content
            )
        }
        bt_open_url.setOnClickListener {
            QRCodeUtils.openUrl(
                activity,
                qrCode!!.resultOfTypeAndValue.value
            )
        }
        bt_call.setOnClickListener {
            val content = qrCode!!.content
//            if (!content.lowercase().startsWith("tel:")) {
//                content = "tel:$content"
//            }
            QRCodeUtils.call(activity, content, qrCode?.resultOfTypeAndValue?.type == T_SMS)

//            PermissionUtils.instance()
//                .showPermissionNormalDialog(context as Activity, object : PermissionCallback {
//                    override fun onPermissionNotGranted() {
//
//                    }
//
//                    override fun onPermissionGranted() {
//                    }
//
//                    override fun onPermissionDenied() {
//                    }
//
//                    override fun onPermissionAborted() {
//                    }
//
//                }, Manifest.permission.CALL_PHONE)
        }
    }

    private fun shareQR(bitmap: Bitmap) {
        val cacheFile = File(context?.cacheDir, Const.FOLDER_CACHE)
        if (!cacheFile.exists()) {
            cacheFile.mkdirs()
        }
        val file = File(cacheFile, "QR_${System.currentTimeMillis()}.png")

        Thread {
            try {
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                    out.flush()
                }
                activity?.runOnUiThread {
                    shareFileImage(file)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun shareFileImage(file: File) {

        val contentUri =
            (context?.packageName + ".provider").let {
                FileProvider.getUriForFile(
                    context!!,
                    it,
                    file
                )
            }

        if (contentUri != null) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, context?.contentResolver?.getType(contentUri))
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            startActivity(Intent.createChooser(shareIntent, "Choose an app"))
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().post("show_banner")
    }

    companion object {
        fun newInstance(qrCode: QRCode, history: History): ResultScanFragment {
            val args = Bundle()
            args.putSerializable("data", qrCode)
            args.putSerializable("history", history)
            val fragment = ResultScanFragment()
            fragment.arguments = args
            return fragment
        }


        fun newInstance(qrCode: QRCode): ResultScanFragment {
            val args = Bundle()
            args.putSerializable("data", qrCode)
            val fragment = ResultScanFragment()
            fragment.arguments = args
            return fragment
        }

        var hasAd: Boolean = false

    }

}



