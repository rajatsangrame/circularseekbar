package com.rajatsangrame.circularseekbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent

class RainbowSeekbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
    listener: OnProgressChangeListener? = null
) : BaseSeekbar(context, attrs, defStyleAttr, defStyleRes, listener) {
    override fun isProgressBarRegion(p: PointF): Boolean {
        TODO("Not yet implemented")
    }

    override fun getSweepAngle(p: PointF): Float {
        TODO("Not yet implemented")
    }

    override fun processTouchEvent(event: MotionEvent): Boolean {
        TODO("Not yet implemented")
    }

    override fun onPreDraw(canvas: Canvas) {
        TODO("Not yet implemented")
    }
}