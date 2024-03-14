package com.rajatsangrame.circularseekbar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.math.MathUtils
import com.rajatsangrame.circularseekbar.Util.dpToPx
import com.rajatsangrame.circularseekbar.Util.pxToDp

abstract class BaseSeekbar(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
    internal var listener: OnProgressChangeListener? = null
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        internal const val TAG = "CircularSeekbar"
        const val DEFAULT_PROGRESS_COLOR = Color.WHITE
        const val DEFAULT_BG_COLOR = Color.GRAY
        const val DEFAULT_THUMB_COLOR = Color.WHITE
        const val DEFAULT_MAX_PROGRESS = 100f
        const val DEFAULT_MIN_PROGRESS = 0f
        const val DEFAULT_THICKNESS_DP = 20
        const val DEFAULT_THUMB_RADIUS_DP = 12
        const val DEFAULT_TOUCHING_PADDING_DP = 8
        const val DEFAULT_PROGRESS = 0.1f
        const val DEFAULT_SHOW_THUMB = true
        const val DEFAULT_ENABLE_TOUCH = true
    }

    protected val rectF = RectF()
    protected var innerRadius = 0f
    protected var outerRadius = 0f
    protected val center = PointF(0f, 0f)

    /**
     * This is used in onTouchEvent to simulate the seekbar progress
     */
    protected var isThumbPressed = false

    internal var progress: Float = DEFAULT_PROGRESS
    internal var maxProgress: Float = DEFAULT_MAX_PROGRESS
    internal var minProgress: Float = DEFAULT_MIN_PROGRESS
    internal var touchPadding = DEFAULT_TOUCHING_PADDING_DP.dpToPx
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

    private fun setThickness(@Px size: Float) {
        progressPaint.strokeWidth = size
        backgroundPaint.strokeWidth = size
        thickness = size
    }

    fun getThickness() = thickness.pxToDp

    fun setProgressChangeListener(listener: OnProgressChangeListener?) {
        this.listener = listener
    }

    @JvmOverloads
    fun setProgress(progress: Float, fromUser: Boolean = false) {
        val range = minProgress..maxProgress
        if (progress !in range) {
            val e =
                IllegalArgumentException("Progress $progress is out of range. It must lie between $minProgress to $maxProgress")
            Log.e(TAG, "setProgress: ", e)
        }
        val p = MathUtils.clamp(progress, minProgress, maxProgress)
        this.progress = p
        listener?.onProgressChanged(this, p, fromUser)
        invalidate()
    }

    @JvmOverloads
    fun setAnimatedProgress(progress: Float, duration: Long = 500L) {
        val p = MathUtils.clamp(progress, minProgress, maxProgress)
        val animator = ValueAnimator.ofFloat(this.progress, p)
        animator.duration = duration

        animator.addUpdateListener { animation ->
            this.progress = animation.animatedValue as Float
            listener?.onProgressChanged(this, this.progress, false)
            invalidate()
        }
        animator.start()
    }

    fun getProgress() = progress

    fun setMaximumProgress(progress: Float) {
        this.maxProgress = progress
    }

    fun setMinimumProgress(progress: Float) {
        if (progress >= this.maxProgress) {
            val e =
                IllegalArgumentException("Minimum progress $progress, must be less than maximum progress $maxProgress")
            Log.e(TAG, "setMinimumProgress: $e")
            return
        }
        this.minProgress = progress
    }

    fun getMaximumProgress() = this.maxProgress


    override fun setBackgroundColor(@ColorInt color: Int) {
        backgroundPaint.color = color
    }

    /**
     * It created [SweepGradient] using color gradients as background color.
     *  Example,
     *
     *      seekbar.setBackgroundGradient(
     *          intArrayOf(Color.RED, Color.YELLOW, Color.WHITE, Color.GREEN),
     *          floatArrayOf(0f, 0.333f, 0.667f, 1f)
     *      )
     *
     * @param colors An array of [ColorInt]. There must be at least 2 colors in the array.
     * @param positions The relative position of each corresponding color in the colors array,
     * beginning with 0 and ending with 1.0.
     * If positions is NULL, then the colors are automatically spaced evenly.
     */
    @JvmOverloads
    fun setBackgroundGradient(colors: IntArray, positions: FloatArray? = null) {
        val shader: Shader = SweepGradient(0f, 0f, colors, positions)
        backgroundPaint.setShader(shader)
    }

    fun removeBackgroundGradient() {
        backgroundPaint.setShader(null)
    }

    fun setProgressColor(@ColorInt color: Int) {
        progressPaint.color = color
    }

    /**
     * It created [SweepGradient] using color gradients as progress color.
     *  Example,
     *
     *      seekbar.setProgressGradient(
     *          intArrayOf(Color.RED, Color.YELLOW, Color.WHITE, Color.GREEN),
     *          floatArrayOf(0f, 0.333f, 0.667f, 1f)
     *      )
     *
     * @param colors An array of [ColorInt]. There must be at least 2 colors in the array.
     * @param positions The relative position of each corresponding color in the colors array,
     * beginning with 0 and ending with 1.0.
     * If positions is NULL, then the colors are automatically spaced evenly.
     */
    @JvmOverloads
    fun setProgressGradient(colors: IntArray, positions: FloatArray? = null) {
        val shader: Shader = SweepGradient(0f, 0f, colors, positions)
        progressPaint.setShader(shader)
    }

    fun removeProgressGradient() {
        progressPaint.setShader(null)
    }

    fun setThumbColor(@ColorInt color: Int) {
        thumbPaint.color = color
    }

    /**
     * It created [SweepGradient] using color gradients as thumb color.
     *  Example,
     *
     *      seekbar.setThumbGradient(
     *          intArrayOf(Color.RED, Color.YELLOW, Color.WHITE, Color.GREEN),
     *          floatArrayOf(0f, 0.333f, 0.667f, 1f)
     *      )
     *
     * @param colors An array of [ColorInt]. There must be at least 2 colors in the array.
     * @param positions The relative position of each corresponding color in the colors array,
     * beginning with 0 and ending with 1.0.
     * If positions is NULL, then the colors are automatically spaced evenly.
     */
    @JvmOverloads
    fun setThumbGradient(colors: IntArray, positions: FloatArray? = null) {
        val shader: Shader = SweepGradient(0f, 0f, colors, positions)
        thumbPaint.setShader(shader)
    }

    fun removeThumbGradient() {
        thumbPaint.setShader(null)
    }

    fun setThumbRadius(radius: Int) {
        setThumbRadius(radius.dpToPx)
    }

    private fun setThumbRadius(@Px radius: Float) {
        thumbRadius = radius
    }

    fun getThumbRadius() = thumbRadius.pxToDp

    /**
     *  Adjust the touch padding surface according. This can be useful if
     *  thumb radius or seekbar thickness is small and difficult to touch
     */
    fun setTouchPadding(padding: Int) {
        setTouchPadding(padding.dpToPx)
    }

    private fun setTouchPadding(@Px padding: Float) {
        this.touchPadding = padding
    }

    private fun getTouchPadding() = this.touchPadding.pxToDp

    fun setShowThumb(boolean: Boolean) {
        this.showThumb = boolean
    }

    fun getShowThumb() = showThumb

    fun setEnableTouch(boolean: Boolean) {
        this.enableTouch = boolean
    }

    fun getEnableTouch() = enableTouch

    protected abstract fun isProgressBarRegion(p: PointF): Boolean

    protected abstract fun getProgressAngle(p: PointF): Float

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
                    DEFAULT_TOUCHING_PADDING_DP.dpToPx
                )
            val thumbRadius =
                array.getDimension(
                    R.styleable.BaseSeekbar_thumbRadius,
                    DEFAULT_THUMB_RADIUS_DP.dpToPx
                )

            val progress =
                array.getFloat(R.styleable.BaseSeekbar_progress, DEFAULT_PROGRESS)
            val minProgress =
                array.getFloat(R.styleable.BaseSeekbar_minProgress, DEFAULT_MIN_PROGRESS)
            val maxProgress =
                array.getFloat(R.styleable.BaseSeekbar_maxProgress, DEFAULT_MAX_PROGRESS)

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
            setTouchPadding(thumbPadding)
            this.showThumb = showThumb
            this.enableTouch = enableTouch
            this.minProgress = minProgress
            this.maxProgress = maxProgress

            val p = MathUtils.clamp(progress, minProgress, maxProgress)
            this.progress = p

            array.recycle()
        }
        listener?.onProgressChanged(this@BaseSeekbar, progress, false)
    }
}