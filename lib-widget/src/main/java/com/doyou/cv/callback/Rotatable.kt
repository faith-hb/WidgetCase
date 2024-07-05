package com.doyou.cv.callback

/**
 * 监听设备旋转角度
 * @autor hongbing
 * @date 2019-10-08
 */
interface Rotatable {
    // Set parameter 'animation' to true to have animation when rotation.
    fun setOrientation(orientation: Int, animation: Boolean)
}