package com.rajatsangrame.circularseekbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import com.rajatsangrame.circularseekbar.Util.dpToPx
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class CircularSeekbar : View {

    private val rectF = RectF()
    private var innerRadius = 0f
    private var outerRadius = 0f
    private val center = PointF(0f, 0f)
    private var isThumbTouchEvent = false

    private var progress: Float = 0f

    private var thumbPadding = 8.dpToPx
    private var thickness = 20.dpToPx
    private var thumbRadius = 10.dpToPx

    private var showThumb = true
    private var disableTouchEvent = false


    private val thumbPaint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.FILL_AND_STROKE
        it.color = Color.WHITE
    }

    private val backgroundPaint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.STROKE
        it.strokeWidth = this.thickness
        it.color = Color.GRAY
    }

    private val progressPaint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.STROKE
        it.strokeWidth = this.thickness
        it.color = Color.WHITE
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        center.x = width / 2f
        center.y = height / 2f
        outerRadius = min(center.x, center.y)
        innerRadius = outerRadius - thickness / 2

        rectF.set(
            (center.x - innerRadius),
            (center.y - innerRadius),
            (center.x + innerRadius),
            (center.y + innerRadius)
        )
        val sweepAngle = (360 * progress) / 100
        canvas.drawArc(rectF, -90f, 360f, false, backgroundPaint)
        canvas.drawArc(rectF, -90f, sweepAngle, false, progressPaint)
        if (showThumb) {
            val sweepAngleRadians = Math.toRadians(-90f + sweepAngle.toDouble())
            val cx = rectF.centerX() + (rectF.width() / 2) * cos(sweepAngleRadians)
            val cy = rectF.centerY() + (rectF.height() / 2) * sin(sweepAngleRadians)
            canvas.drawCircle(cx.toFloat(), cy.toFloat(), thumbRadius, thumbPaint)
        }
    }

    fun setThickness(size: Int) {
        progressPaint.strokeWidth = size.dpToPx
        backgroundPaint.strokeWidth = size.dpToPx
        thickness = size.dpToPx
    }

    fun setProgress(progress: Float) {
        val range = 1f..100f
        if (progress !in range) {
            throw IllegalArgumentException("$progress is out of range. It should lies between 1 ot  to 100")
        }
        this.progress = progress
        invalidate()
    }

    fun getProgress() = progress

    fun setAnimatedProgress(progress: Float) {

    }


    override fun setBackgroundColor(@ColorInt color: Int) {
        backgroundPaint.color = color
    }

    fun setProgressColor(@ColorInt color: Int) {
        progressPaint.color = color
    }

    fun setThumbColor(@ColorInt color: Int) {
        thumbPaint.color = color
    }

    fun setThumbRadius(radius: Int) {
        thumbRadius = radius.dpToPx
    }

    fun setThumbPadding(padding: Float) {

        val range = 1f..100f
        if (padding !in range) {
            throw IllegalArgumentException("$padding is out of range. It should lies between 1 ot  to 100")
        }
        this.thumbPadding = padding
    }

    /**
     * Check the distance of point xy from the center of the circle
     * when the (0,0) is top left corner of the plane
     */
    private fun isProgressBarRegion(p: PointF): Boolean {
        val distance = sqrt((p.x - center.x).pow(2) + (p.y - center.y).pow(2))
        return (distance in (innerRadius - thumbPadding)..(outerRadius + thumbPadding))
    }

    private fun getSweepAngle(p: PointF): Float {
        val angleRadians = kotlin.math.atan2(p.y - center.y, p.x - center.x)
        var angleDegrees = Math.toDegrees(angleRadians.toDouble())
        if (angleDegrees < 0) {
            // Convert negative angles to positive
            angleDegrees += 360.0
        }
        // Adjust so that 12 o'clock is 0 degrees
        angleDegrees -= 270.0
        if (angleDegrees < 0) {
            // Convert negative angles to positive
            angleDegrees += 360.0
        }
        return angleDegrees.toFloat()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (disableTouchEvent) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                val x = event.x
                val y = event.y
                val p = PointF(x, y)
                if (isProgressBarRegion(p)) {
                    isThumbTouchEvent = true
                }
                return true
            }

            MotionEvent.ACTION_UP -> {
                isThumbTouchEvent = false
                return true
            }

            MotionEvent.ACTION_MOVE -> {

                if (!isThumbTouchEvent) return true

                val x = event.x
                val y = event.y
                val p = PointF(x, y)
                val angleDegrees = getSweepAngle(p)
                progress = (angleDegrees * 100f) / 360f
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    enum class StartAngle(value: Float) {
        UP(-90f),
        LEFT(-180f),
        BOTTOM(-270f),
        RIGHT(0f),
    }
}