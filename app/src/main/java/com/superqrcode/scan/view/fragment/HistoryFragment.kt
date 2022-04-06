package com.superqrcode.scan.view.fragment

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.control.interfaces.AdCallback
import com.common.control.manager.AdmobManager
import com.docxmaster.docreader.base.BaseFragment
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.superqrcode.scan.App
import com.superqrcode.scan.BuildConfig
import com.superqrcode.scan.R
import com.superqrcode.scan.StorageCommon
import com.superqrcode.scan.model.Favourite
import com.superqrcode.scan.model.History
import com.superqrcode.scan.view.OnActionCallback
import com.superqrcode.scan.view.activity.ResultScanActivity
import com.superqrcode.scan.view.adapter.FavouriteAdapter
import com.superqrcode.scan.view.adapter.HistoryAdapter
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment(
    override val layoutId: Int = R.layout.fragment_history
) :
    BaseFragment(), OnActionCallback {
    private var favouriteAdapter: FavouriteAdapter? = null
    private var favouriteList: MutableList<Favourite>? = null
    private var historyAdapter: HistoryAdapter? = null
    private var historyList: MutableList<History?>? = null
    override fun onResume() {
        super.onResume()
        if (StorageCommon.instance?.interResultHistory == null) {
            loadInterResult()
        }
        fetchData()
    }

    private fun fetchData() {
//        favouriteList = App.getInstance().database.favoriteDao().list
//        favouriteAdapter?.updateList(favouriteList!!)

        historyList = App.getInstance().database.historyDao().list
        historyAdapter?.updateList(historyList!!)
    }

    override fun initView() {
        historyAdapter = HistoryAdapter(historyList, context)
        historyAdapter?.mCallback = this
        rv_history.layoutManager = LinearLayoutManager(context)
        rv_history.adapter = historyAdapter

//        favouriteAdapter = FavouriteAdapter(favouriteList, context)
//        rv_favourite.layoutManager = LinearLayoutManager(context)
//        rv_favourite.adapter = favouriteAdapter
    }

    override fun addEvent() {
    }

    companion object {
        fun newInstance(): HistoryFragment {
            val args = Bundle()

            val fragment = HistoryFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun callback(key: String?, vararg data: Any?) {
        var count = StorageCommon.instance?.countScan
        ResultScanFragment.hasAd = (count == 0)
        val history = (data[0] as History)
        context?.let { ResultScanActivity.start(it, history.qrCode, history) }
        if (count == 0) {
            showInterFileAds()
            StorageCommon.instance?.countScan = 1
        } else {
            count = count?.minus(1)
            StorageCommon.instance?.countScan = count!!
        }
    }

    private fun showInterFileAds() {
        AdmobManager.getInstance().showInterstitial(
            context as Activity?,
            StorageCommon.instance?.interResultHistory,
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
                BuildConfig.inter_result_history,
                object : AdCallback() {
                    override fun interCallback(interstitialAd: InterstitialAd) {
                        super.interCallback(interstitialAd)
                        StorageCommon.instance?.interResultHistory = interstitialAd
                    }

                    override fun onAdFailedToLoad(i: LoadAdError) {
                        super.onAdFailedToLoad(i)
                        StorageCommon.instance?.interResultHistory = null
                    }
                })
    }

}