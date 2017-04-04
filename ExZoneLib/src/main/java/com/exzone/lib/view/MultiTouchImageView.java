package com.exzone.lib.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 作者:李鸿浩
 * 描述:手势放大、缩小，旋转图片控件
 * 时间:2017/4/4.
 */
public class MultiTouchImageView extends AppCompatImageView {
    private int Img_ID;
    private float startDis;// 开始距离
    private PointF midPoint;// 中间点
    private float oldRotation = 0;
    private float rotation = 0;
    private PointF startPoint = new PointF();
    private Matrix matrix = new Matrix();
    private Matrix currentMatrix = new Matrix();
    private Activity mActivity;
    private boolean is_Editable = true;
    public int width;
    public int height;
    float matrixX, matrixY;
    float saveScale = 1f;
    float minScale = 1f;
    float maxScale = 3f;
    float redundantXSpace, redundantYSpace;
    float right, bottom, origWidth, origHeight, bmWidth, bmHeight;
    float[] m;

    /**
     * 模式 NONE：无 DRAG：拖拽. ZOOM:缩放
     */
    private enum MODE {
        NONE, DRAG, ZOOM

    }

    private MODE mode = MODE.NONE;// 默认模式

    public MultiTouchImageView(Context context) {
        this(context, null);
    }

    public MultiTouchImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiTouchImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    /**
     * 控制图片的可编辑性
     **/
    public void setEditable(boolean is_Editable) {
        this.is_Editable = is_Editable;
    }

    public boolean getEditable() {
        return this.is_Editable;
    }

    /**
     * 计算两点之间的距离
     */
    public static float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 计算两点之间的中间点
     */
    public static PointF mid(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);

    }

    /***
     * touch 事件
     */
    private int lastX, lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //处理单点、多点触摸
        if (is_Editable) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:// 手指压下屏幕
                    mode = MODE.DRAG;
                    currentMatrix.set(this.getImageMatrix());// 记录ImageView当前的移动位置
                    matrix.set(currentMatrix);
                    startPoint.set(event.getX(), event.getY());
                    postInvalidate();
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:// 当屏幕上还有触点（手指），再有一个手指压下屏幕
                    mode = MODE.ZOOM;
                    oldRotation = rotation(event);
                    startDis = distance(event);
                    if (startDis > 10f) {
                        midPoint = mid(event);
                        currentMatrix.set(this.getImageMatrix());// 记录ImageView当前的缩放倍数
                    }
                    break;
                case MotionEvent.ACTION_MOVE:// 手指在屏幕移动，该 事件会不断地触发
                    if (mode == MODE.DRAG) {
                        float dx = event.getX() - startPoint.x;// 得到在x轴的移动距离
                        float dy = event.getY() - startPoint.y;// 得到在y轴的移动距离
                        matrix.set(currentMatrix);// 在没有进行移动之前的位置基础上进行移动
                        matrix.postTranslate(dx, dy);
                    } else if (mode == MODE.ZOOM) {// 缩放与旋转
                        float endDis = distance(event);// 结束距离
                        rotation = (rotation(event) - oldRotation);
                        if (endDis > 10f) {
                            float scale = endDis / startDis;// 得到缩放倍数
                            matrix.set(currentMatrix);
                            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                            matrix.postRotate(rotation, midPoint.x, midPoint.y);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:// 手指离开屏
                    // 设置不能出界
                    int dx = (int) event.getRawX() - lastX;
                    int dy = (int) event.getRawY() - lastY;
                    int left = this.getLeft() + dx;
                    int top = this.getTop() + dy;
                    int right = this.getRight() + dx;
                    if (left < 0) {
                        left = 0;
                        right = left + this.getWidth();
                    }
                    if (right > width) {
                        right = (int) width;
                        left = right - this.getWidth();
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:// 有手指离开屏幕,但屏幕还有触点（手指）
                    mode = MODE.NONE;
                    break;
            }
            this.setImageMatrix(matrix);
        }
        return true;
    }

    private void calcPadding() {
        right = width * saveScale - width - (2 * redundantXSpace * saveScale);
        bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
    }

    private void fillMatrixXY() {
        matrix.getValues(m);
        matrixX = m[Matrix.MTRANS_X];
        matrixY = m[Matrix.MTRANS_Y];
    }

    private void scaleMatrixToBounds() {
        if (Math.abs(matrixX + right / 2) > 0.5f)
            matrix.postTranslate(-(matrixX + right / 2), 0);
        if (Math.abs(matrixY + bottom / 2) > 0.5f)
            matrix.postTranslate(0, -(matrixY + bottom / 2));
    }

    public Bitmap getImageBitmap() {
        // ImageView对象必须做如下设置后，才能获取其中的图像
        setDrawingCacheEnabled(true);
        // 获取ImageView中的图像
        Bitmap obmp = Bitmap.createBitmap(getDrawingCache());
        //从ImageView对象中获取图像后，要记得调用setDrawingCacheEnabled(false)清空画图缓
        //冲区，否则，下一次用getDrawingCache()方法回去图像时，还是原来的图像
        setDrawingCacheEnabled(false);
        return obmp;
    }
}
