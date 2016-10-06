package com.exzone.lib.widget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.View;

import com.exzone.lib.R;

/**
 * 作者:李鸿浩
 * 描述:https://github.com/rubensousa/RecyclerViewSnap
 * 时间: 2016/9/30.
 */
public class GravitySnapHelper extends SnapHelper {

    private static final float INVALID_DISTANCE = 1f;

    private OrientationHelper mVerticalHelper;
    private OrientationHelper mHorizontalHelper;
    private int mGravity;
    private boolean mIsRtlHorizontal;
    private boolean mSnapLastItemEnabled;

    public GravitySnapHelper(int gravity) {
        this(gravity, false);
    }

    public GravitySnapHelper(int gravity, boolean enableSnapLastItem) {
        if (gravity != Gravity.START && gravity != Gravity.END
                && gravity != Gravity.BOTTOM && gravity != Gravity.TOP) {
            throw new IllegalArgumentException("Invalid gravity value. Use START " + "| END | BOTTOM | TOP constants");
        }
        mGravity = gravity;
        mSnapLastItemEnabled = enableSnapLastItem;
    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        if (recyclerView != null && (mGravity == Gravity.START || mGravity == Gravity.END)) {
//            mIsRtlHorizontal = recyclerView.getContext().getResources().getBoolean(R.bool.is_rtl);
        }
        super.attachToRecyclerView(recyclerView);
    }

    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        return new int[0];
    }

    @Nullable
    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        return null;
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        return 0;
    }
}
