package com.rajatsangrame.circularseekbar

import android.content.res.Resources

internal object Util {
    val Int.dpToPx: Float get() = (this * Resources.getSystem().displayMetrics.density)
}
