package com.rajatsangrame.circularseekbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class CircularSeekbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
    private var listener: OnProgressChangeListener? = null
) : BaseSeekbar(context, attrs, defStyleAttr, defStyleRes) {

    private var startAngle: StartAngle = StartAngle.TOP

    fun setStartAngle(startAngle: StartAngle) {
        this.startAngle = startAngle
    }

    fun getStartAngle() = startAngle


    override fun onPreDraw(canvas: Canvas) {
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
        val sweepAngle = (360 * progress) / 100
        canvas.drawArc(rectF, startAngle.value, 360f, false, backgroundPaint)
        canvas.drawArc(rectF, startAngle.value, sweepAngle, false, progressPaint)
        if (showThumb) {
            val sweepAngleRadians = Math.toRadians(startAngle.value + sweepAngle.toDouble())
            val cx = rectF.centerX() + (rectF.width() / 2) * cos(sweepAngleRadians)
            val cy = rectF.centerY() + (rectF.height() / 2) * sin(sweepAngleRadians)
            canvas.drawCircle(cx.toFloat(), cy.toFloat(), thumbRadius, thumbPaint)
        }
    }


    /**
     * Check the distance of point xy from the center of the circle
     */
    override fun isProgressBarRegion(p: PointF): Boolean {
        val distance = sqrt((p.x - center.x).pow(2) + (p.y - center.y).pow(2))
        // selecting max touch area according to padding or thumb radius
        val padding = max(thumbPadding, thumbRadius * 2)
        return (distance in (innerRadius - padding)..(outerRadius + padding))
    }

    override fun getSweepAngle(p: PointF): Float {
        val angleRadians = kotlin.math.atan2(p.y - center.y, p.x - center.x)
        var angleDegrees = Math.toDegrees(angleRadians.toDouble()).toFloat()
        if (angleDegrees < 0) {
            // Convert negative angles to positive
            angleDegrees += 360.0f
        }
        // Adjust values according to startAngle
        angleDegrees -= 360f + startAngle.value
        if (angleDegrees < 0) {
            // Convert agiain if negative values exists
            angleDegrees += 360.0f
        }
        return angleDegrees
    }

    init {
        attrs?.let {
            val array = context.obtainStyledAttributes(it, R.styleable.CircularSeekbar)
            val startAngle: StartAngle =
                StartAngle.get(array.getInteger(R.styleable.CircularSeekbar_startAngle, 0))
            this.startAngle = startAngle
            array.recycle()
        }
    }

    override fun processTouchEvent(event: MotionEvent): Boolean {
        if (!enableTouch) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                val x = event.x
                val y = event.y
                val p = PointF(x, y)
                if (isProgressBarRegion(p)) {
                    isThumbPressed = true
                }
                listener?.onStartTouchEvent(this@CircularSeekbar)
            }

            MotionEvent.ACTION_UP -> {
                isThumbPressed = false
                listener?.onStopTouchEvent(this@CircularSeekbar)
            }

            MotionEvent.ACTION_MOVE -> {

                if (!isThumbPressed) return true

                val x = event.x
                val y = event.y
                val p = PointF(x, y)
                val angleDegrees = getSweepAngle(p)
                progress = (angleDegrees * 100f) / 360f
                listener?.onProgressChanged(this@CircularSeekbar, progress, true)
                invalidate()
            }
        }
        return true
    }
}