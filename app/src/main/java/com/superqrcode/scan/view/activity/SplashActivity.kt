package com.superqrcode.scan.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.common.control.interfaces.AdCallback
import com.common.control.manager.AdmobManager
import com.docxmaster.docreader.base.BaseActivity
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.superqrcode.scan.BuildConfig
import com.superqrcode.scan.R

@SuppressLint("CustomSplashScreen")
class SplashActivity(override val layoutId: Int = R.layout.activity_splash) : BaseActivity() {
    override fun initView() {
        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                AdmobManager.getInstance()
                    .loadInterAds(
                        this@SplashActivity,
                        BuildConfig.inter_splash,
                        object : AdCallback() {
                            override fun interCallback(interstitialAd: InterstitialAd?) {
                                showInter(interstitialAd)
                            }

                            override fun onAdFailedToLoad(i: LoadAdError?) {
                                super.onAdFailedToLoad(i)
                                startMain()
                            }
                        })
            }
        }, 2000)

    }

    private fun showInter(interstitialAd: InterstitialAd?) {
        if (ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            AdmobManager.getInstance()
                .showInterstitial(this, interstitialAd, object : AdCallback() {
                    override fun onAdClosed() {
                        startMain()
                    }


                })
        } else {
            startMain()
        }
    }

    private fun startMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun addEvent() {
    }
}