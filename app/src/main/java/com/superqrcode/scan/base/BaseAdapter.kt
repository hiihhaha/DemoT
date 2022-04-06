package com.docxmaster.docreader.base

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.superqrcode.scan.databinding.ItemDemoBinding
import com.superqrcode.scan.view.OnActionCallback

//import com.documentmaster.documentscan.OnActionCallback

abstract class BaseAdapter<T>(protected var mList: MutableList<T>?,  var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    var mCallback: OnActionCallback? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return viewHolder(parent, viewType)
    }

    protected abstract fun viewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        onBindView(viewHolder, position)
    }

    protected abstract fun onBindView(viewHolder: RecyclerView.ViewHolder?, position: Int)
    override fun getItemCount(): Int {
        if (mList == null) return 0 else return mList!!.size
    }

}