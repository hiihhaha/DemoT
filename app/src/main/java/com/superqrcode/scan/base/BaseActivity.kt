package com.docxmaster.docreader.base

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.superqrcode.scan.Const
import com.superqrcode.scan.R
import com.superqrcode.scan.utils.CommonUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        initLanguage()
        initView()
        addEvent()
    }

    protected fun initLanguage() {}

    protected abstract val layoutId: Int
    protected abstract fun initView()
    protected abstract fun addEvent()


    protected fun saveBitmap(bitmap: Bitmap) {
        val dir = CommonUtils.getInstance().getDocumentDirPath(Const.FOLDER_NAME)
        val file = File(
            dir.absolutePath + "/QR" + CommonUtils.getInstance().formatDate2(
                System.currentTimeMillis()
            ) + ".png"
        )
        Thread {
            try {
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                    out.flush()
                    out.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
        Toast.makeText(
            this,
            getString(R.string.save_qr_to) + file.absolutePath,
            Toast.LENGTH_SHORT
        ).show()
    }


    protected open fun shareQR(bitmap: Bitmap) {
        val cacheFile = File(cacheDir, Const.FOLDER_CACHE)
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
            } catch (e: IOException) {
                e.printStackTrace()
            }
            shareFileImage(file)
        }.start()
    }

    private fun shareFileImage(file: File) {

        val contentUri =
            ("$packageName.provider").let {
                FileProvider.getUriForFile(
                    this,
                    it,
                    file
                )
            }

        if (contentUri != null) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, contentResolver?.getType(contentUri))
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            startActivity(Intent.createChooser(shareIntent, "Choose an app"))
        }
    }

}