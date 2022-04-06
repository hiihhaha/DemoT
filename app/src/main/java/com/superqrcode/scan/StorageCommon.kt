package com.superqrcode.scan

import com.google.android.gms.ads.interstitial.InterstitialAd
import com.superqrcode.scan.StorageCommon

//import com.google.android.gms.ads.InterstitialAd;
class StorageCommon private constructor() {
    var interResult: InterstitialAd? = null
    var interResultHistory: InterstitialAd? = null
    var countScan = 0

    companion object {
        var instance: StorageCommon? = null
            get() {
                if (field == null) {
                    field = StorageCommon()
                }
                return field
            }
            private set
    }
}