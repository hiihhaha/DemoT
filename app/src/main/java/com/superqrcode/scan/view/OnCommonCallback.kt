package com.superqrcode.scan.view

import android.text.Editable
import android.text.TextWatcher
import android.view.animation.Animation
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener

abstract class OnCommonCallback : Animation.AnimationListener, TextWatcher,
    OnSeekBarChangeListener {
    override fun onAnimationStart(animation: Animation) {}
    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
    override fun onAnimationEnd(animation: Animation) {}
    override fun onAnimationRepeat(animation: Animation) {}
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {}
}