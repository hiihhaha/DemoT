package com.superqrcode.scan.extension

import android.text.Editable
import com.superqrcode.scan.R
import com.superqrcode.scan.view.OnCommonCallback
import com.superqrcode.scan.view.widget.CustomEditText

fun CustomEditText.addEventEdittext(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(
        drawable,
        0,
        0,
        0
    )
    this.addTextChangedListener(object : OnCommonCallback() {
        override fun afterTextChanged(s: Editable) {
            super.afterTextChanged(s)
            if (s.isNotEmpty()) {
                this@addEventEdittext.setCompoundDrawablesWithIntrinsicBounds(
                    drawable,
                    0,
                    R.drawable.ic_clear_text,
                    0
                )
            } else {
                this@addEventEdittext.setCompoundDrawablesWithIntrinsicBounds(
                    drawable,
                    0,
                    0,
                    0
                )
            }
        }
    })
    this@addEventEdittext.setDrawableClickListener {
        if (it == CustomEditText.DrawableClickListener.DrawablePosition.RIGHT) {
            this@addEventEdittext.setText("")
        }
    }




}

