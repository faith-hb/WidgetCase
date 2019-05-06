package com.doyou.cv.widget.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.dongni.tools.Common;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * 自定义可拖拽的FloatingActionButton(悬浮按钮)
 * @autor hongbing
 * @date 2018/9/28
 */
public class DragFloatActionButton extends FloatingActionButton {

    /**
     * 自定义点击接口
     */
    private OnClickListener mListener;

    public void setFabClickListener(OnClickListener listener) {
        mListener = listener;
    }

    private int _parentHeight, _parentWidth = 0;
    private int _xDown, _yDown; // 按下的点坐标
    private int _xDelta, _yDelta; // 随着移动不断变化的点坐标
    private boolean _isClick; // 是否判定为点击事件

    public DragFloatActionButton(Context context) {
        this(context, null);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        Common.log_d("201809281348","view->dispatchTouchEvent -> action = " + event.getAction());
//        /**
//         * super.dispatchTouchEvent(event)不能放到log里面执行，会影响事件的正常传递，比如此处：return true会不起效果
//         */
////        Common.log_d("201809281348","view->dispatchTouchEvent -> action = " + super.dispatchTouchEvent(event));
//        return super.dispatchTouchEvent(event);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Common.log_d("201809281348", "view->onTouchEvent ->action = " + event.getAction());
        final int rawX = (int) event.getRawX();
        final int rawY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
                _isClick = true;
                _xDown = rawX;
                _yDown = rawY;
                _xDelta = rawX;
                _yDelta = rawY;
                ViewGroup parent;
                if (getParent() != null) {
                    parent = (ViewGroup) getParent();
                    _parentHeight = parent.getHeight();
                    _parentWidth = parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = rawX - _xDelta;
                int dy = rawY - _yDelta;
                float x = getX() + dx;
                float y = getY() + dy;
                //检测是否到达边缘 左上右下
                x = x < 0 ? 0 : x > _parentWidth - getWidth() ? _parentWidth - getWidth() : x;
                y = getY() < 0 ? 0 : getY() + getHeight() > _parentHeight ? _parentHeight - getHeight() : y;
                setX(x);
                setY(y);
                _xDelta = rawX;
                _yDelta = rawY;
                break;
            case MotionEvent.ACTION_UP:
                setPressed(false);
                int upX = rawX - _xDown;
                int upY = rawY - _yDown;
                if (Math.abs(upX) > 6 || Math.abs(upY) > 6) { // 移动了
                    _isClick = false;
                }
                if (mListener != null && _isClick) {
                    mListener.onClick(this);
                }
                _xDown = 0;
                _yDown = 0;
                break;
            default:
                break;
        }

        return true; // 自身消费 false不消费会导致移动无效果，super默认是消费的，但事件会继续往下传递，所以此处最佳是true
    }
}
