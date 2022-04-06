package com.superqrcode.scan.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.docxmaster.docreader.base.BaseAdapter
import com.superqrcode.scan.databinding.ItemImageBinding
import com.superqrcode.scan.model.Image
import com.superqrcode.scan.view.activity.PreviewImageActivity
import java.io.File

class SavedQRAdapter(mList: MutableList<Image?>?, context: Context?) :
    BaseAdapter<Image?>(mList, context!!) {
    override fun viewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FavouriteViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindView(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        if (viewHolder is FavouriteViewHolder) {
            mList?.get(position)?.let { viewHolder.load(it) }
        }
    }

    private inner class FavouriteViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        @SuppressLint("SetTextI18n")
        fun load(image: Image) {
            itemView.tag = image
            Glide.with(context).load(image.image).into(binding.ivIcon)
            binding.tvName.text = File(image.image).name
        }

        init {
            itemView.setOnClickListener { v: View? ->
                val image = itemView.tag as Image
                PreviewImageActivity.start(context)
            }
        }
    }
}