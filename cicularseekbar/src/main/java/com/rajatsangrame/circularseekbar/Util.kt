package com.rajatsangrame.circularseekbar

import android.content.res.Resources

object Util {

    internal val Int.dpToPx: Float get() = this * Resources.getSystem().displayMetrics.density
    internal val Float.pxToDp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()
    fun BaseSeekbar.onProgressChanged(callback: (Float, Boolean) -> Unit) {
        this.setProgressChangeListener(object : OnProgressChangeListener {
            override fun onProgressChanged(
                seekBar: BaseSeekbar,
                progress: Float,
                fromUser: Boolean
            ) {
                callback(progress, fromUser)
            }

            override fun onStartTouchEvent(seekBar: BaseSeekbar) {}

            override fun onStopTouchEvent(seekBar: BaseSeekbar) {}

        })
    }
}
