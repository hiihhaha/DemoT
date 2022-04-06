package com.superqrcode.scan.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.docxmaster.docreader.base.BaseAdapter
import com.superqrcode.scan.App
import com.superqrcode.scan.Const
import com.superqrcode.scan.R
import com.superqrcode.scan.databinding.ItemHistoryBinding
import com.superqrcode.scan.model.Favourite
import com.superqrcode.scan.model.History
import com.superqrcode.scan.utils.QRCodeUtils

class HistoryAdapter(mList: MutableList<History?>?, context: Context?) :
    BaseAdapter<History?>(mList, context!!) {
    override fun viewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HistoryViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindView(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        if (viewHolder is HistoryViewHolder) {
            mList?.get(position)?.let { viewHolder.load(it) }
        }
    }

    fun updateList(favouriteList: MutableList<History?>) {
        this.mList = favouriteList
        notifyDataSetChanged()
    }

    private inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        @SuppressLint("SetTextI18n")
        fun load(history: History) {
            try {
                binding.tvContent.text = history.qrCode.resultOfTypeAndValue.value
            } catch (e: Exception) {
                e.printStackTrace()
            }
            history.isFavourite = checkFavourite(history.id)
            binding.tvName.text = history.qrCode.name
            Glide.with(context).load(QRCodeUtils.getIcon(history.qrCode.resultOfTypeAndValue.type))
                .into(
                    binding.ivIcon
                )
            if (history.isFavourite) {
                binding.ivFavorite.setImageResource(R.drawable.ic_favourite_actived)
            } else {
                binding.ivFavorite.setImageResource(R.drawable.ic_favourite)
            }
            itemView.tag = history
        }

        private fun checkFavourite(id: Int): Boolean {
            val list = App.getInstance().database.favoriteDao().list
            list.forEach {
                if (it.historyId == id) return true
            }
            return false
        }

        init {
            binding.ivFavorite.setOnClickListener { v: View? ->
                val history = itemView.tag as History
                if (history.isFavourite) {
                    App.getInstance().database.favoriteDao().delete(history.id)
                } else {
                    App.getInstance().database.favoriteDao().add(Favourite(history.id))
                }
                history.isFavourite = !history.isFavourite
                mList?.let { notifyItemChanged(it.indexOf(history)) }
            }
            itemView.setOnClickListener { v: View? ->
                if (mCallback != null) {
                    mCallback?.callback(Const.K_CLICK_ITEM, itemView.tag)
                }
            }
        }
    }
}