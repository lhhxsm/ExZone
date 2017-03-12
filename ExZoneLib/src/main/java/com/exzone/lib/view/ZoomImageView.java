package com.exzone.lib.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;

/**
 * 作者:李鸿浩
 * 描述:自定义可以放大缩小、移动的图片控件。
 * 时间: on 2017/3/12.
 */
public class ZoomImageView extends AppCompatImageView implements ViewTreeObserver.OnGlobalLayoutListener, ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {
    private boolean mOnce = false;
    /**
     * 初始化时的缩放值
     */
    private float mInitScale;

    /**
     * 双击放大时的缩放值
     */
    private float mMidScale;

    /**
     * 放大的最大缩放值
     */
    private float mMaxScale;

    /**
     *
     */
    private Matrix mScaleMatrix;

    /**
     * 捕获多点触控时的缩放比例
     */
    private ScaleGestureDetector mScaleGestureDetector;

    //**********自由移动*************/

    /**
     * 记录上一次多点触控的数量
     */
    private int mLastPointerCount;

    /**
     * 多点触控的最后位置
     */
    private float mLastX;
    private float mLastY;

    private int mTouchSlop;
    private boolean isCanDrag;//是否可以移动

    private boolean isCheckLeftAndRight = false;
    private boolean isCheckTopAndBottom = false;


    //**********双击放大或者缩小*************/
    private GestureDetector mGestureDetector;
    private boolean isAutoScale = false;//正在缩放

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaleMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(this);
        /*
         *表示滑动的时候,手的移动要大于这个距离才开始移动控件
         */
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (isAutoScale) {
                    return true;
                }
                float x = e.getX();
                float y = e.getY();
                isAutoScale = true;
                if (getScale() < mMidScale) {
                    //mScaleMatrix.postScale(mMidScale / getScale(), mMidScale / getScale(), x, y);
                    //setImageMatrix(mScaleMatrix);
                    postDelayed(new AutoScaleRunnable(mMidScale, x, y), 16);
                } else {
                    //mScaleMatrix.postScale(mInitScale / getScale(), mInitScale / getScale(), x, y);
                    //setImageMatrix(mScaleMatrix);
                    postDelayed(new AutoScaleRunnable(mInitScale, x, y), 16);
                }
                return true;
            }
        });
    }


    private class AutoScaleRunnable implements Runnable {
        /**
         * 缩放的目标值
         */
        private float mTargetScale;
        /**
         * 缩放的中心点
         */
        private float x;
        private float y;
        /**
         * 放大和缩小的系数
         */
        private final float BIGGER = 1.07f;
        private final float SMALL = 0.93f;

        private float tmpScale;

        public AutoScaleRunnable(float targetScale, float x, float y) {
            this.mTargetScale = targetScale;
            this.x = x;
            this.y = y;
            if (getScale() < mTargetScale) {
                tmpScale = BIGGER;
            }
            if (getScale() > mTargetScale) {
                tmpScale = SMALL;
            }
        }

        @Override
        public void run() {
            //进行缩放
            mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
            float currentScale = getScale();

            if ((tmpScale > 1.0f && currentScale < mTargetScale) || (tmpScale < 1.0f && currentScale > mTargetScale)) {//继续进行缩放
                postDelayed(this, 16);
            } else {//设置为我们的目标值
                float scale = mTargetScale / currentScale;
                mScaleMatrix.postScale(scale, scale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);
                isAutoScale = false;
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    /**
     * 获取ImageView加载完整的图片
     */
    @Override
    public void onGlobalLayout() {
        if (!mOnce) {
            //获取控件的宽和高
            int width = getWidth();
            int height = getHeight();

            //获取图片的宽和高
            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }
            int dw = drawable.getIntrinsicWidth();
            int dh = drawable.getIntrinsicHeight();
            float scale = 1.0f;
            /*
             * 图片宽度大于控件宽度,图片高度小于控件高度,将其缩小
             */
            if (dw > width && dh < height) {
                scale = width * 1.0f / dw;
            }
            /*
             *图片宽度小于控件宽度,图片高度大于控件高度,将其缩小
             */
            if (dw < width && dh > height) {
                scale = height * 1.0f / dh;
            }

            if ((dw > width && dh > height) || (dw < width && dh < height)) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }
            /*
             * 缩放的初始值
             */
            mInitScale = scale;
            mMaxScale = mInitScale * 4;
            mMidScale = mInitScale * 2;

            //将图片移动到控件中心
            int dx = getWidth() / 2 - dw / 2;
            int dy = getHeight() / 2 - dh / 2;

            mScaleMatrix.postTranslate(dx, dy);
            mScaleMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
            setImageMatrix(mScaleMatrix);
            mOnce = true;
        }
    }

    /**
     * 获取当前图片的缩放值
     */
    public float getScale() {
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    //缩放区间 initScale maxScale
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();//缩放值
        if (getDrawable() == null) {
            return true;
        }
        //缩放范围的控制
        if ((scale < mMaxScale && scaleFactor > 1.0f) || (scale > mInitScale && scaleFactor < 1.0f)) {
            if (scale * scaleFactor < mInitScale) {//保证缩放的最小值是mInitScale
                scaleFactor = mInitScale / scale;
            }

            if (scale * scaleFactor > mMaxScale) {//保证缩放的最大值是mMaxScale
                scaleFactor = mMaxScale / scale;
            }

            //缩放
            mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
        }
        return true;
    }

    /**
     * 获取图片放大缩小以后的宽和高,以及t,b,l,r
     */
    private RectF getMatrixRectF() {
        Matrix matrix = mScaleMatrix;
        RectF rectF = new RectF();
        Drawable drawable = getDrawable();
        if (drawable != null) {
            rectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    /**
     * 在缩放的时候进行边界控制以及我们的位置控制
     */
    private void checkBorderAndCenterWhenScale() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        int width = getWidth();
        int height = getHeight();
        /*
         *缩放时进行边界检测,防止出现白边
         */
        //水平方向
        if (rectF.width() >= width) {
            if (rectF.left > 0) {//左边有缝隙
                deltaX = -rectF.left;
            }

            if (rectF.right < width) {//右边有缝隙
                deltaX = width - rectF.right;
            }
        }
        //垂直方向
        if (rectF.width() >= height) {
            if (rectF.top > 0) {//上边有缝隙
                deltaY = -rectF.top;
            }

            if (rectF.bottom < height) {//下边有缝隙
                deltaY = height - rectF.bottom;
            }
        }

        //如果宽度或者高度小于控件的宽或者高,则让其居中
        if (rectF.width() < width) {
            deltaX = width / 2 - rectF.right + rectF.width() / 2;
        }
        if (rectF.height() < height) {
            deltaY = height / 2 - rectF.bottom + rectF.height() / 2;
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (mGestureDetector.onTouchEvent(event)) {//双击
            return true;
        }

        mScaleGestureDetector.onTouchEvent(event);//将触摸传递给多点触控监听

        //多点触控 中心点的坐标位置
        float x = 0;
        float y = 0;
        //获取多点触控的数量
        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }

        x /= pointerCount;
        y /= pointerCount;

        if (mLastPointerCount != pointerCount) {
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }
        mLastPointerCount = pointerCount;
        RectF rectF = getMatrixRectF();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://图片没有完整显示的时候,触摸事件将不被其他控件拦截
                if (rectF.width() > getWidth() + 0.01 || rectF.height() > getHeight() + 0.01) {//+ 0.01 因为计算可能产生的误差
                    if (getParent() instanceof ViewPager) {
                        getParent().requestDisallowInterceptTouchEvent(true);//不允许父控件进行拦截事件
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (rectF.width() > getWidth() + 0.01 || rectF.height() > getHeight() + 0.01) {
                    if (getParent() instanceof ViewPager) {
                        getParent().requestDisallowInterceptTouchEvent(true);//不允许父控件进行拦截事件
                    }
                }

                float dx = x - mLastX;
                float dy = y - mLastY;

                if (!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy);
                }

                if (isCanDrag) {
                    if (getDrawable() != null) {

                        isCheckLeftAndRight = true;
                        isCheckTopAndBottom = true;

                        //如果宽度小于控件宽度,不允许水平方向移动
                        if (rectF.width() < getWidth()) {
                            isCheckLeftAndRight = false;
                            dx = 0;
                        }
                        //如果高度小于控件高度,不允许垂直方向移动
                        if (rectF.height() < getHeight()) {
                            isCheckTopAndBottom = false;
                            dy = 0;
                        }

                        mScaleMatrix.postTranslate(dx, dy);
                        checkBorderWhenTranslate();
                        setImageMatrix(mScaleMatrix);
                    }
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointerCount = 0;
                break;


            default:
                break;
        }
        return true;
    }

    /**
     * 移动时,进行边界检查
     */
    private void checkBorderWhenTranslate() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        int width = getWidth();
        int height = getHeight();

        if (rectF.left > 0 && isCheckLeftAndRight) {
            deltaX = -rectF.left;
        }
        if (rectF.right < width && isCheckLeftAndRight) {
            deltaX = width - rectF.right;
        }

        if (rectF.top > 0 && isCheckTopAndBottom) {
            deltaY = -rectF.top;
        }
        if (rectF.bottom < height && isCheckTopAndBottom) {
            deltaY = height - rectF.bottom;
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);

    }

    /**
     * 判断是否是Move
     *
     * @param dx x方向偏移量
     * @param dy y向偏移量
     * @return true移动
     */
    private boolean isMoveAction(float dx, float dy) {
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }
}
