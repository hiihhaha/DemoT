package com.superqrcode.scan.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.docxmaster.docreader.base.BaseAdapter
import com.superqrcode.scan.Const
import com.superqrcode.scan.databinding.ItemMenuBinding
import com.superqrcode.scan.model.MenuItem

class MenuItemAdapter(mList: MutableList<MenuItem?>?, context: Context?) :
    BaseAdapter<MenuItem?>(mList, context!!) {
    override fun viewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MenuItemViewHolder(
            ItemMenuBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindView(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        val holder = viewHolder as MenuItemViewHolder
        mList?.get(position)?.let { holder.loadData(it) }
    }

    private inner class MenuItemViewHolder(private val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun loadData(menuItem: MenuItem) {
            itemView.tag = menuItem
            binding.tvTitle.text = menuItem.name
            Glide.with(context).load(menuItem.icon).into(binding.ivIcon)
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