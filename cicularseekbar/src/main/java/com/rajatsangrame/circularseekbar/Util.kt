package com.rajatsangrame.circularseekbar

object Util {

    fun CircularSeekbar.onProgressChanged(callback: (Float, Boolean) -> Unit) {
        this.setProgressChangeListener(object : CircularSeekbar.OnProgressChangeListener {
            override fun onProgressChanged(
                seekBar: CircularSeekbar,
                progress: Float,
                fromUser: Boolean
            ) {
                callback(progress, fromUser)
            }

            override fun onStartTouchEvent(seekBar: CircularSeekbar) {}

            override fun onStopTouchEvent(seekBar: CircularSeekbar) {}

        })
    }
}
