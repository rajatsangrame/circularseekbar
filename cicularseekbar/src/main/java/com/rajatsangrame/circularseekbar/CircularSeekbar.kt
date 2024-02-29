package com.rajatsangrame.circularseekbar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.rajatsangrame.circularseekbar.Util.dpToPx
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
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val TAG = "CircularSeekbar"
        const val DEFAULT_PROGRESS_COLOR = Color.WHITE
        const val DEFAULT_BG_COLOR = Color.GRAY
        const val DEFAULT_THUMB_COLOR = Color.WHITE
        const val DEFAULT_THICKNESS_PX = 20f
        const val DEFAULT_THUMB_RADIUS_PX = 10f
        const val DEFAULT_THUMB_PADDING_PX = 4f
        const val DEFAULT_PROGRESS = 0.1f
        const val DEFAULT_SHOW_THUMB = true
        const val DEFAULT_ENABLE_TOUCH = true
    }

    private val rectF = RectF()
    private var innerRadius = 0f
    private var outerRadius = 0f
    private val center = PointF(0f, 0f)
    private var isThumbTouchEvent = false

    private var progress: Float = DEFAULT_PROGRESS

    private var thumbPadding = 4.dpToPx
    private var thickness = 20.dpToPx
    private var thumbRadius = 10.dpToPx
    private var startAngle: StartAngle = StartAngle.TOP

    private var showThumb = DEFAULT_SHOW_THUMB
    private var enableTouch = DEFAULT_ENABLE_TOUCH

    private val thumbPaint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.FILL_AND_STROKE
        it.color = DEFAULT_THUMB_COLOR
    }

    private val backgroundPaint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.STROKE
        it.strokeWidth = this.thickness
        it.color = DEFAULT_BG_COLOR
    }

    private val progressPaint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.STROKE
        it.strokeWidth = this.thickness
        it.color = DEFAULT_PROGRESS_COLOR
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        if (left != top || left != bottom || left != right) {
            Log.e(TAG, "setPadding: Padding should be same for all.")
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

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

    fun setStartAngle(startAngle: StartAngle) {
        this.startAngle = startAngle
    }

    fun setThickness(size: Int) {
        setThickness(size.dpToPx)
    }

    private fun setThickness(@Px size: Float) {
        progressPaint.strokeWidth = size
        backgroundPaint.strokeWidth = size
        thickness = size
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

    @JvmOverloads
    fun setAnimatedProgress(progress: Float, duration: Long = 500L) {

        val animator = ValueAnimator.ofFloat(this.progress, progress)
        animator.duration = duration

        animator.addUpdateListener { animation ->
            this.progress = animation.animatedValue as Float
            invalidate()
        }
        animator.start()
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
        setThumbRadius(radius.dpToPx)
    }

    private fun setThumbRadius(@Px radius: Float) {
        thumbRadius = radius
    }

    /**
     *  Adjust the thumb padding surface according. This can be useful if thumb radius
     *  is small and difficult to touch
     */
    fun setThumbPadding(padding: Int) {
        setThumbPadding(padding.dpToPx)
    }

    private fun setThumbPadding(@Px padding: Float) {
        this.thumbPadding = padding
    }

    fun setShowThumb(boolean: Boolean) {
        this.showThumb = boolean
    }

    fun setEnableTouch(boolean: Boolean) {
        this.enableTouch = boolean
    }

    /**
     * Check the distance of point xy from the center of the circle
     * when the (0,0) is top left corner of the plane
     */
    private fun isProgressBarRegion(p: PointF): Boolean {
        val distance = sqrt((p.x - center.x).pow(2) + (p.y - center.y).pow(2))
        // selecting max touch area according to padding or thumb radius
        val padding = max(thumbPadding, thumbRadius * 2)
        return (distance in (innerRadius - padding)..(outerRadius + padding))
    }

    private fun getSweepAngle(p: PointF): Float {
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

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (!enableTouch) return false

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

    sealed class StartAngle(val value: Float) {
        data object TOP : StartAngle(-90f)
        data object LEFT : StartAngle(-180f)
        data object RIGHT : StartAngle(0f)
        data object BOTTOM : StartAngle(-270f)
        companion object {
            fun get(index: Int): StartAngle {
                return when (index) {
                    0 -> LEFT
                    1 -> TOP
                    2 -> RIGHT
                    3 -> BOTTOM
                    else -> throw IllegalArgumentException("Invalid value for StartAngle: $index")
                }
            }
        }
    }

    init {
        attrs?.let {
            val array = context.obtainStyledAttributes(it, R.styleable.CircularSeekbar)
            val bgColor =
                array.getColor(R.styleable.CircularSeekbar_backgroundColor, DEFAULT_BG_COLOR)
            val progressColor = array.getColor(
                R.styleable.CircularSeekbar_progressColor,
                DEFAULT_PROGRESS_COLOR
            )
            val thumbColor =
                array.getColor(R.styleable.CircularSeekbar_thumbColor, DEFAULT_THUMB_COLOR)
            val thickness =
                array.getDimension(R.styleable.CircularSeekbar_thickness, DEFAULT_THICKNESS_PX)
            val thumbPadding =
                array.getDimension(
                    R.styleable.CircularSeekbar_thumbPadding,
                    DEFAULT_THUMB_PADDING_PX
                )
            val thumbRadius =
                array.getDimension(R.styleable.CircularSeekbar_thumbRadius, DEFAULT_THUMB_RADIUS_PX)

            val progress =
                array.getFloat(R.styleable.CircularSeekbar_progress, DEFAULT_PROGRESS)

            val showThumb =
                array.getBoolean(R.styleable.CircularSeekbar_showThumb, DEFAULT_SHOW_THUMB)
            val enableTouch =
                array.getBoolean(
                    R.styleable.CircularSeekbar_enableTouch,
                    DEFAULT_ENABLE_TOUCH
                )
            val startAngle: StartAngle =
                StartAngle.get(array.getInteger(R.styleable.CircularSeekbar_startAngle, 0))

            setBackgroundColor(bgColor)
            setProgressColor(progressColor)
            setThumbColor(thumbColor)
            setThickness(thickness)
            setThumbRadius(thumbRadius)
            setThumbPadding(thumbPadding)
            this.showThumb = showThumb
            this.enableTouch = enableTouch
            this.progress = progress
            this.startAngle = startAngle

            array.recycle()
        }
    }
}