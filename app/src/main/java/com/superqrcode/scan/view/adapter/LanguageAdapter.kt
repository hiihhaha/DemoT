package com.superqrcode.scan.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.common.control.model.Language
import com.docxmaster.docreader.base.BaseAdapter
import com.superqrcode.scan.Const
import com.superqrcode.scan.databinding.ItemLanguageBinding

class LanguageAdapter(mList: MutableList<Language?>?, context: Context?) :
    BaseAdapter<Language?>(mList, context!!) {
    override fun viewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LanguageViewHolder(
            ItemLanguageBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }


    override fun onBindView(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        val holder = viewHolder as LanguageViewHolder
        mList?.get(position)?.let { holder.loadData(it) }
    }

    private inner class LanguageViewHolder(private val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun loadData(language: Language) {
            itemView.tag = language
            Glide.with(context).load("android_asset/flag/" + language.code + ".webp")
                .into(binding.ivIcon)
            binding.tvName.text = language.name
        }

        init {
            itemView.setOnClickListener { v: View? ->
                if (mCallback != null) {
                    mCallback?.callback(Const.K_CLICK_ITEM, itemView.tag)
                }
            }
        }
    }
}