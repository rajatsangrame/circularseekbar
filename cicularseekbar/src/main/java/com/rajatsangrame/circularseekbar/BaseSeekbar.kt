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
import com.rajatsangrame.circularseekbar.Util.pxToDp

abstract class BaseSeekbar(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
    private var listener: OnProgressChangeListener? = null
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val TAG = "CircularSeekbar"
        const val DEFAULT_PROGRESS_COLOR = Color.WHITE
        const val DEFAULT_BG_COLOR = Color.GRAY
        const val DEFAULT_THUMB_COLOR = Color.WHITE
        const val DEFAULT_THICKNESS_DP = 20
        const val DEFAULT_THUMB_RADIUS_DP = 10
        const val DEFAULT_THUMB_PADDING_DP = 4
        const val DEFAULT_PROGRESS = 0.1f
        const val DEFAULT_SHOW_THUMB = true
        const val DEFAULT_ENABLE_TOUCH = true
    }

    internal val rectF = RectF()
    internal var innerRadius = 0f
    internal var outerRadius = 0f
    internal val center = PointF(0f, 0f)

    /**
     * This is used in onTouchEvent to simulate the seekbar progress
     */
    internal var isThumbPressed = false
    internal var progress: Float = DEFAULT_PROGRESS
    internal var thumbPadding = DEFAULT_THUMB_PADDING_DP.dpToPx
    internal var thickness = DEFAULT_THICKNESS_DP.dpToPx
    internal var thumbRadius = DEFAULT_THUMB_RADIUS_DP.dpToPx
    internal var showThumb = DEFAULT_SHOW_THUMB
    internal var enableTouch = DEFAULT_ENABLE_TOUCH
    internal val thumbPaint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.FILL_AND_STROKE
        it.color = DEFAULT_THUMB_COLOR
    }
    protected val backgroundPaint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.STROKE
        it.strokeWidth = this.thickness
        it.color = DEFAULT_BG_COLOR
    }
    protected val progressPaint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.STROKE
        it.strokeWidth = this.thickness
        it.color = DEFAULT_PROGRESS_COLOR
    }

    fun setThickness(size: Int) {
        setThickness(size.dpToPx)
    }

    protected fun setThickness(@Px size: Float) {
        progressPaint.strokeWidth = size
        backgroundPaint.strokeWidth = size
        thickness = size
    }

    fun getThickness() = thickness.pxToDp

    fun setProgressChangeListener(listener: OnProgressChangeListener) {
        this.listener = listener
    }

    fun removeProgressChangeListener() {
        this.listener = null
    }

    @JvmOverloads
    fun setProgress(progress: Float, fromUser: Boolean = false) {
        val range = 1f..100f
        if (progress !in range) {
            throw IllegalArgumentException("$progress is out of range. It should lies between 1 ot  to 100")
        }
        this.progress = progress
        listener?.onProgressChanged(this, progress, fromUser)
        invalidate()
    }

    @JvmOverloads
    fun setAnimatedProgress(progress: Float, duration: Long = 500L) {

        val animator = ValueAnimator.ofFloat(this.progress, progress)
        animator.duration = duration

        animator.addUpdateListener { animation ->
            this.progress = animation.animatedValue as Float
            listener?.onProgressChanged(this, this.progress, false)
            invalidate()
        }
        animator.start()
    }

    fun getProgress() = progress


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

    protected fun setThumbRadius(@Px radius: Float) {
        thumbRadius = radius
    }

    fun getThumbRadius() = thumbRadius.pxToDp

    /**
     *  Adjust the thumb padding surface according. This can be useful if thumb radius
     *  is small and difficult to touch
     */
    fun setThumbPadding(padding: Int) {
        setThumbPadding(padding.dpToPx)
    }

    protected fun setThumbPadding(@Px padding: Float) {
        this.thumbPadding = padding
    }

    private fun getThumbPadding() = this.thumbPadding.pxToDp

    fun setShowThumb(boolean: Boolean) {
        this.showThumb = boolean
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return processTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        onPreDraw(canvas)

    }


    fun getShowThumb() = showThumb

    fun setEnableTouch(boolean: Boolean) {
        this.enableTouch = boolean
    }

    fun getEnableTouch() = enableTouch

    protected abstract fun isProgressBarRegion(p: PointF): Boolean

    protected abstract fun getSweepAngle(p: PointF): Float

    protected abstract fun processTouchEvent(event: MotionEvent): Boolean

    protected abstract fun onPreDraw(canvas: Canvas)

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        if (left != top || left != bottom || left != right) {
            Log.e(TAG, "setPadding: Padding should be same for all.")
        }
    }

    init {
        attrs?.let {
            val array = context.obtainStyledAttributes(it, R.styleable.BaseSeekbar)
            val bgColor =
                array.getColor(R.styleable.BaseSeekbar_backgroundColor, DEFAULT_BG_COLOR)
            val progressColor = array.getColor(
                R.styleable.BaseSeekbar_progressColor,
                DEFAULT_PROGRESS_COLOR
            )
            val thumbColor =
                array.getColor(R.styleable.BaseSeekbar_thumbColor, DEFAULT_THUMB_COLOR)
            val thickness =
                array.getDimension(
                    R.styleable.BaseSeekbar_thickness,
                    DEFAULT_THICKNESS_DP.dpToPx
                )
            val thumbPadding =
                array.getDimension(
                    R.styleable.BaseSeekbar_thumbPadding,
                    DEFAULT_THUMB_PADDING_DP.dpToPx
                )
            val thumbRadius =
                array.getDimension(
                    R.styleable.BaseSeekbar_thumbRadius,
                    DEFAULT_THUMB_RADIUS_DP.dpToPx
                )

            val progress =
                array.getFloat(R.styleable.BaseSeekbar_progress, DEFAULT_PROGRESS)

            val showThumb =
                array.getBoolean(R.styleable.BaseSeekbar_showThumb, DEFAULT_SHOW_THUMB)
            val enableTouch =
                array.getBoolean(
                    R.styleable.BaseSeekbar_enableTouch,
                    DEFAULT_ENABLE_TOUCH
                )

            setBackgroundColor(bgColor)
            setProgressColor(progressColor)
            setThumbColor(thumbColor)
            setThickness(thickness)
            setThumbRadius(thumbRadius)
            setThumbPadding(thumbPadding)
            this.showThumb = showThumb
            this.enableTouch = enableTouch
            this.progress = progress

            array.recycle()
        }
        listener?.onProgressChanged(this@BaseSeekbar, progress, false)
    }
}