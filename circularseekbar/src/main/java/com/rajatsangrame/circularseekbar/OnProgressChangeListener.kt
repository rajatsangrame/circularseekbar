package com.rajatsangrame.circularseekbar

interface OnProgressChangeListener {
    fun onProgressChanged(seekBar: BaseSeekbar, progress: Float, fromUser: Boolean)

    fun onStartTouchEvent(seekBar: BaseSeekbar)

    fun onStopTouchEvent(seekBar: BaseSeekbar)
}