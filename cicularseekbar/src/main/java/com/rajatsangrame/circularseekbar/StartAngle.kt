package com.rajatsangrame.circularseekbar

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