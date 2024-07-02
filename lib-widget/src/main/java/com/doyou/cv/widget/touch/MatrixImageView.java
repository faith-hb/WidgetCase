//package com.doyou.cv.widget.touch;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.graphics.PointF;
//import android.support.v7.widget.AppCompatImageView;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//
//import com.doyou.cv.R;
//import com.doyou.cv.utils.Utils;
//
//import java.util.Arrays;
//
///**
// * @autor hongbing
// * @date 2019/1/26
// */
//public class MatrixImageView extends AppCompatImageView {
//
//    /**
//     * 默认的触摸模式
//     */
//    private static final int MODE_NONE = 0x00123;
//    /**
//     * 拖拽模式
//     */
//    private static final int MODE_DRAG = 0x00321;
//    /**
//     * 缩放or旋转模式
//     */
//    private static final int MODE_ZOOM = 0x00132;
//
//    private Context mContext;
//    private int mode; // 当前的触摸模式
//    private float proMove = 1f; // 上一次手指移动的距离
//    private float saveRotate = 0f; // 保存了的角度值
//    private float rotate = 0f; // 旋转的角度
//
//    private float[] preEventCoor; // 上一次各触摸点的坐标集合
//    private PointF start, mid; // 起点、中点对象
//    private Matrix currentMatrix, savedMatrix; // 当前和保存了的Matrix对象
//
//    public MatrixImageView(Context context) {
//        this(context, null);
//    }
//
//    public MatrixImageView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        mContext = context;
//        // 初始化
//        init();
//    }
//
//    private void init() {
//        int screenW = getResources().getDisplayMetrics().widthPixels;
//        int screenH = getResources().getDisplayMetrics().heightPixels;
//
//        /**
//         * 实例化对象
//         */
//        currentMatrix = new Matrix();
//        savedMatrix = new Matrix();
//        start = new PointF();
//        mid = new PointF();
//
//        // 模式初始化
//        mode = MODE_NONE;
//
//        /**
//         * 设置图片资源
//         */
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.jingy);
//        bitmap = Bitmap.createScaledBitmap(bitmap, screenW, screenH, true);
//        setImageBitmap(bitmap);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN: // 单点接触屏幕时
//                savedMatrix.set(currentMatrix);
//                start.set(event.getX(), event.getY());
//                mode = MODE_DRAG;
//                preEventCoor = null;
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN: // 第二个点接触屏幕时
//                proMove = calSpacing(event);
//                if (proMove > 10f) {
//                    savedMatrix.set(currentMatrix);
//                    calMidPoint(mid, event);
//                    mode = MODE_ZOOM;
//                }
//                preEventCoor = new float[4];
//                preEventCoor[0] = event.getX(0);
//                preEventCoor[1] = event.getX(1);
//                preEventCoor[2] = event.getY(0);
//                preEventCoor[3] = event.getY(1);
//                saveRotate = calRotation(event);
//                break;
//            case MotionEvent.ACTION_UP: // 单点离开屏幕时
//                Utils.logD("201801261717","ACTION_UP");
//                mode = MODE_NONE;
//                preEventCoor = null;
//                break;
//            case MotionEvent.ACTION_POINTER_UP: // 第二个点离开屏幕时
//                Utils.logD("201801261717", "ACTION_POINTER_UP");
//                mode = MODE_DRAG;
//                savedMatrix.set(currentMatrix);
////                preEventCoor = null;
//                Utils.logD("201901261749", Arrays.toString(preEventCoor));
//                break;
//            case MotionEvent.ACTION_MOVE: // 触摸点移动时
//                /**
//                 * 单点触摸拖拽平移
//                 */
//                if (mode == MODE_DRAG) {
//                    currentMatrix.set(savedMatrix);
//                    float dx = event.getX() - start.x;
//                    float dy = event.getY() - start.y;
//                    currentMatrix.postTranslate(dx, dy);
//                }
//                /**
//                 * 两点触控拖放旋转
//                 */
//                else if (mode == MODE_ZOOM) {
//                    if (event.getPointerCount() >= 2) {
//                        float currentMove = calSpacing(event);
//                        /**
//                         * 指尖移动距离大于10f缩放
//                         */
//                        if (currentMove > 10f) {
//                            float scale = currentMove / proMove;
//                            currentMatrix.postScale(scale, scale, mid.x, mid.y);
//                        }
//                        /**
//                         * 保持两点时旋转
//                         */
//                        if (preEventCoor != null) {
//                            rotate = calRotation(event);
//                            float r = rotate - saveRotate;
//                            currentMatrix.postRotate(r, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
//                        }
//                    } else {
//                        mode = MODE_DRAG;
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//        setImageMatrix(currentMatrix);
//        return true;
//    }
//
//    /**
//     * 计算两个触摸点间的距离
//     * @param event
//     * @return
//     */
//    private float calSpacing(MotionEvent event) {
//        float x = event.getX(0) - event.getX(1);
//        float y = event.getY(0) - event.getY(1);
//        return (float) Math.sqrt(x * x + y * y);
//    }
//
//    /**
//     * 计算两个触摸点的中点坐标
//     *
//     * @param pointF
//     * @param event
//     */
//    private void calMidPoint(PointF pointF, MotionEvent event) {
//        float x = event.getX(0) + event.getX(1);
//        float y = event.getY(0) + event.getY(1);
//        pointF.set(x / 2, y / 2);
//    }
//
//    /**
//     * 计算旋转角度
//     *
//     * @param event
//     */
//    private float calRotation(MotionEvent event) {
//        double deltaX = event.getX(0) - event.getX(1);
//        double deltaY = event.getY(0) - event.getY(1);
//        double radius = Math.atan2(deltaY, deltaX);
//        return (float) Math.toDegrees(radius);
//    }
//}
