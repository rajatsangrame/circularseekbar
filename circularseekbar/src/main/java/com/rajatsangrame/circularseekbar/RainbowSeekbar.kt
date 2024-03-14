package com.rajatsangrame.circularseekbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.core.math.MathUtils
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

open class RainbowSeekbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
    listener: OnProgressChangeListener? = null
) : BaseSeekbar(context, attrs, defStyleAttr, defStyleRes, listener) {

    private var sweepAngle = DEFAULT_SWEEP_ANGLE

    fun setSweepAngle(sweepAngle: Float) {
        val range = 1f..360f
        if (sweepAngle !in range) {
            val e =
                IllegalArgumentException("Sweep angle $sweepAngle is out of range. It should lies between 1 to 360")
            Log.e(TAG, "setSweepAngle: ", e)
        }
        val angle = MathUtils.clamp(sweepAngle, 0f, 360f)
        this.sweepAngle = angle
    }

    private fun getSweepAngle() = sweepAngle


    fun setRoundCorners(boolean: Boolean) {
        val cap = if (boolean) Paint.Cap.ROUND else Paint.Cap.BUTT
        backgroundPaint.strokeCap = cap
        progressPaint.strokeCap = cap
    }

    override fun isProgressBarRegion(p: PointF): Boolean {
        val distance = sqrt((p.x - center.x).pow(2) + (p.y - center.y).pow(2))
        // selecting max touch area according to padding or thumb radius
        val padding = max(touchPadding, thumbRadius * 2)
        val validDistance = (distance in (innerRadius - padding)..(outerRadius + padding))
        val angle = getProgressAngle(p)
        return validDistance && (angle in 0f..sweepAngle)
    }

    /**
     * @return Angle displacement from 0 to 360 degrees wrt sweep angle
     */
    override fun getProgressAngle(p: PointF): Float {
        val angleRadians = kotlin.math.atan2(p.y - center.y, p.x - center.x)
        var angleDegrees = Math.toDegrees(angleRadians.toDouble()).toFloat()
        if (angleDegrees < 0) {
            // Convert negative angles to positive
            angleDegrees += 360.0f
        }
        // Adjust values according to startAngle
        angleDegrees -= 360f + StartAngle.BOTTOM.value
        // Adjust values according to sweep angle
        angleDegrees -= (360f - sweepAngle) / 2
        if (angleDegrees < 0) {
            // Convert agiain if negative values exists
            angleDegrees += 360.0f
        }
        return angleDegrees
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!enableTouch) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                val x = event.x
                val y = event.y
                val p = PointF(x, y)
                if (isProgressBarRegion(p)) {
                    isThumbPressed = true
                }
                listener?.onStartTouchEvent(this@RainbowSeekbar)
                return true
            }

            MotionEvent.ACTION_UP -> {
                isThumbPressed = false
                listener?.onStopTouchEvent(this@RainbowSeekbar)
                return true
            }

            MotionEvent.ACTION_MOVE -> {

                if (!isThumbPressed) return true

                val x = event.x
                val y = event.y
                val p = PointF(x, y)
                val angleDegrees = getProgressAngle(p)
                val tempProgress =
                    minProgress + (angleDegrees * (maxProgress - minProgress) / sweepAngle)

                // Allow padding angle of 10 degree as touch event will
                // not reach min max limit accurately
                val sweepAnglePadding = 10f
                if (tempProgress in minProgress..maxProgress) {
                    progress = tempProgress
                } else if (angleDegrees in sweepAngle..sweepAngle + sweepAnglePadding) {
                    progress = maxProgress
                } else if (angleDegrees in 360f - sweepAnglePadding..360f) {
                    progress = minProgress
                } else return false

                listener?.onProgressChanged(this@RainbowSeekbar, progress, true)
                invalidate()
                return true
            }
        }
        return false
    }

    override fun onDraw(canvas: Canvas) {
        center.x = width / 2f
        center.y = height / 2f

        outerRadius = min(center.x - paddingLeft, center.y - paddingRight)
        innerRadius = outerRadius - thickness / 2

        rectF.set(
            (center.x - innerRadius),
            (center.y - innerRadius),
            (center.x + innerRadius),
            (center.y + innerRadius)
        )
        val progressAngle = sweepAngle * (progress - minProgress) / (maxProgress - minProgress)

        val startAngle = -(sweepAngle / 2) - 90
        canvas.drawArc(rectF, startAngle, sweepAngle, false, backgroundPaint)
        canvas.drawArc(rectF, startAngle, progressAngle, false, progressPaint)

        if (showThumb) {
            val sweepAngleRadians = Math.toRadians(startAngle + progressAngle.toDouble())
            val cx = rectF.centerX() + (rectF.width() / 2) * cos(sweepAngleRadians)
            val cy = rectF.centerY() + (rectF.height() / 2) * sin(sweepAngleRadians)
            canvas.drawCircle(cx.toFloat(), cy.toFloat(), thumbRadius, thumbPaint)
        }
    }

    init {
        attrs?.let {
            val array = context.obtainStyledAttributes(it, R.styleable.RainbowSeekbar)
            val sweepAngle =
                array.getFloat(R.styleable.RainbowSeekbar_sweepAngle, DEFAULT_SWEEP_ANGLE)
            val roundCorners =
                array.getBoolean(R.styleable.RainbowSeekbar_roundCorners, true)
            setRoundCorners(roundCorners)
            this.sweepAngle = sweepAngle
            array.recycle()
        }
    }

    companion object {
        const val DEFAULT_SWEEP_ANGLE = 270F
    }

}