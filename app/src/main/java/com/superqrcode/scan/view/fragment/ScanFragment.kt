package com.superqrcode.scan.view.fragment

import android.Manifest
import android.app.Activity
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.common.control.interfaces.AdCallback
import com.common.control.interfaces.PermissionCallback
import com.common.control.manager.AdmobManager
import com.common.control.utils.PermissionUtils
import com.docxmaster.docreader.base.BaseFragment
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.superqrcode.scan.App
import com.superqrcode.scan.BuildConfig
import com.superqrcode.scan.R
import com.superqrcode.scan.StorageCommon
import com.superqrcode.scan.Type.Companion.T_URL
import com.superqrcode.scan.model.History
import com.superqrcode.scan.model.QRCode
import com.superqrcode.scan.utils.CodeGenerator
import com.superqrcode.scan.utils.QRCodeUtils
import com.superqrcode.scan.utils.SharePreferenceUtils
import com.superqrcode.scan.view.activity.MainActivity
import com.superqrcode.scan.view.activity.ResultScanActivity
import com.superqrcode.scan.view.dialog.IntroScanDialog
import kotlinx.android.synthetic.main.fragment_scan.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*

class ScanFragment(override val layoutId: Int = R.layout.fragment_scan) : BaseFragment() {
    private var zXingScannerView: ZXingScannerView? = null
    private val resultHandler = ZXingScannerView.ResultHandler { rawResult: Result? ->
        executeLoadCode(rawResult)
    }

    private fun showInterFileAds() {
        AdmobManager.getInstance().showInterstitial(
            context as Activity?,
            StorageCommon.instance?.interResult,
            object : AdCallback() {
                override fun onAdClosed() {
                    super.onAdClosed()
                    loadInterResult()
                }
            })
    }

    private fun loadInterResult() {
        AdmobManager.getInstance()
            .loadInterAds(
                context as Activity?,
                BuildConfig.inter_result_scan,
                object : AdCallback() {
                    override fun interCallback(interstitialAd: InterstitialAd) {
                        super.interCallback(interstitialAd)
                        StorageCommon.instance?.interResult = interstitialAd
                    }

                    override fun onAdFailedToLoad(i: LoadAdError) {
                        super.onAdFailedToLoad(i)
                        StorageCommon.instance?.interResult = null
                    }
                })
    }

    private var isFlash = false
    private var isFrontCam = false
    private fun executeLoadCode(result: Result?) {
        if (result == null) {
            return
        }
        playSound()
        val typeOfCode = result.barcodeFormat.toString()
        val text = result.text
        val isQRCode = typeOfCode.contains("AZTEC") || typeOfCode.contains("PDF_417") ||
                typeOfCode.contains("DATA_MATRIX") || typeOfCode.contains("QR_CODE")
        val qrCode = QRCode(text, if (isQRCode) CodeGenerator.TYPE_QR else CodeGenerator.TYPE_BAR)
        copyText(qrCode.resultOfTypeAndValue.value)
        if (SharePreferenceUtils.isOpenUrlAuto(context)
            && qrCode.resultOfTypeAndValue.type == T_URL
        ) {
            QRCodeUtils.openUrl(context as Activity?, qrCode.content)
        }
        App.getInstance().database.historyDao().add(History(qrCode, System.currentTimeMillis()))
        var count = StorageCommon.instance?.countScan
        ResultScanFragment.hasAd = (count == 1)
        context?.let { ResultScanActivity.start(it, qrCode, null) }
        Log.d("AndroidLog", "handleResult: $count")
        if (count == 1) {
            showInterFileAds()
            StorageCommon.instance?.countScan = 0
        } else {
            count = count?.plus(1)
            StorageCommon.instance?.countScan = count!!
        }
    }

    private fun copyText(value: String) {
        if (!SharePreferenceUtils.isCopy(context)) {
            return
        }
        QRCodeUtils.copyToClipboard(context, value)
    }

    private fun playSound() {
        if (!SharePreferenceUtils.isBeep(context)) {
            return
        }
        val mediaPlayer = MediaPlayer.create(context, R.raw.sound)
        mediaPlayer.start()
    }

    override fun onResume() {
        super.onResume()
        try {
            if (StorageCommon.instance?.interResult == null) {
                loadInterResult()
            }
            initZxing()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openGallery() {
        try {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initZxing() {
        if (context == null) {
            return
        }
        zXingScannerView = ZXingScannerView(context)
        zXingScannerView!!.setSquareViewFinder(true)
        try {
            zXingScannerView!!.setResultHandler(resultHandler)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                getString(R.string.not_support),
                Toast.LENGTH_SHORT
            ).show()
        }
        activateScanner()
        zXingScannerView!!.setAutoFocus(SharePreferenceUtils.isFocus(context))
        zXingScannerView!!.resumeCameraPreview(resultHandler)
    }

    private fun activateScanner() {
        try {
            if (zXingScannerView != null) {
                if (zXingScannerView!!.parent != null) {
                    (zXingScannerView!!.parent as ViewGroup).removeView(zXingScannerView) // to prevent crush on re adding view
                }
                fr_content.removeAllViews()
                fr_content.addView(zXingScannerView)
                if (zXingScannerView!!.isActivated) {
                    zXingScannerView!!.stopCamera()
                }
                try {
                    zXingScannerView!!.startCamera(0)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun initView() {
        AdmobManager.getInstance().loadBanner(context as Activity, BuildConfig.banner_home)
        context?.registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    if (zXingScannerView == null) {
                        return
                    }
                    if (MainActivity.pauseScan) {
                        zXingScannerView?.stopCamera()
                    } else {
                        initZxing()
                    }
                }
            },
            IntentFilter("ACTION_CAMERA")
        )

        PermissionUtils.instance().showPermissionNormalDialog(context as Activity?, object :
            PermissionCallback {
            override fun onPermissionNotGranted() {
            }

            override fun onPermissionGranted() {
                if (SharePreferenceUtils.isFirstTime(context)) {
                    context?.let { IntroScanDialog.start(it) }
                }
            }

            override fun onPermissionDenied() {
                Log.d("android_log", ": onPermissionDenied");
                showDialogSetting()

            }

            override fun onPermissionAborted() {
                Log.d("android_log", ": onPermissionAborted");
            }

        }, Manifest.permission.CAMERA)
    }


    private fun showDialogSetting() {
        val builder = context?.let {
            AlertDialog.Builder(it)
        }
        builder?.setTitle("Permission request")
        builder?.setMessage("Please go to Setting to Allow Camera Permission to Scan QR!")
        builder?.setCancelable(false)
        builder?.setNegativeButton("Cancel") { dialogInterface: DialogInterface?, i: Int ->

        }
        builder?.setPositiveButton("Allow") { dialogInterface: DialogInterface?, i: Int ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context?.packageName, null)
            intent.data = uri
            startActivityForResult(intent, 1)
        }
        builder?.create()?.show()
    }

    override fun addEvent() {
        ll_flash.setOnClickListener {
            isFlash = !isFlash
            if (isFlash) {
                iv_flash.setImageResource(R.drawable.ic_flash_active)
            } else {
                iv_flash.setImageResource(R.drawable.ic_flash)
            }
            zXingScannerView!!.flash = isFlash
        }
        ll_gallery.setOnClickListener { openGallery() }
        ll_switch.setOnClickListener {
            zXingScannerView!!.stopCamera()
            if (isFrontCam) {
                zXingScannerView!!.startCamera(Camera.CameraInfo.CAMERA_FACING_BACK)
                ll_flash.visibility = View.VISIBLE
            } else {
                zXingScannerView!!.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT)
                ll_flash.visibility = View.INVISIBLE
            }
            isFrontCam = !isFrontCam
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            executeImageUri(data)
        }
    }

    private fun executeImageUri(imageReturnedIntent: Intent) {
        val selectedImage = imageReturnedIntent.data
        var baseStream: InputStream? = null
        var imageStream: InputStream? = null
        try {
            //getting the image
            baseStream = selectedImage?.let { context?.contentResolver?.openInputStream(it) }
            imageStream = selectedImage?.let { context?.contentResolver?.openInputStream(it) }
        } catch (e: FileNotFoundException) {
            Toast.makeText(context, "Error file", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

        //decoding bitmap for get width
        val base = BitmapFactory.decodeStream(baseStream)
        //set options for resize image
        val options = BitmapFactory.Options()
        if (base != null) {
            options.inSampleSize = base.width / 1000
        }
        try {
            val bMap =
                BitmapFactory.decodeStream(imageStream, null, options) //get image with options
            val stream = ByteArrayOutputStream()
            bMap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            val reader: Reader = MultiFormatReader() // use this otherwise
            val tmpHintsMap: MutableMap<DecodeHintType, Any?> = EnumMap(
                DecodeHintType::class.java
            )
            tmpHintsMap[DecodeHintType.TRY_HARDER] = true //thorough search Qrcode

            //rotate image if nothing found
            var result: Result? = null
            for (i in 0..7) {
                val bitmap = binaryBitmapFromJpegData(byteArray, i * 45)
                try {
                    //get result
                    result = reader.decode(bitmap, tmpHintsMap)
                    break
                } catch (e: NotFoundException) {
                    if (i == 7) {
                        Toast.makeText(context, "Not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            executeLoadCode(result)
        } catch (e: Exception) {
            Toast.makeText(context, "Not found", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun binaryBitmapFromJpegData(data: ByteArray, rotation: Int): BinaryBitmap {
        var bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
        if (rotation != 0) {
            bitmap = rotateBitmap(bitmap, rotation.toFloat())
        }
        var intArray = IntArray(0)
        try {
            intArray = IntArray(bitmap.width * bitmap.height)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        val source: LuminanceSource = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
        return BinaryBitmap(HybridBinarizer(source))
    }

    companion object {
        fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(angle)
            return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
        }

        fun newInstance(): ScanFragment {
            val args = Bundle()
            val fragment = ScanFragment()
            fragment.arguments = args
            return fragment
        }
    }
}