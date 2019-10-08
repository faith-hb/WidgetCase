package com.doyou.cv.callback;

/**
 * 监听设备旋转角度
 * @autor hongbing
 * @date 2019-10-08
 */
public interface Rotatable {
    // Set parameter 'animation' to true to have animation when rotation.
    void setOrientation(int orientation,boolean animation);
}
