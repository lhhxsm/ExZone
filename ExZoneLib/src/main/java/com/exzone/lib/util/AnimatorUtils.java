package com.exzone.lib.util;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.exzone.lib.R;

/**
 * 作者:李鸿浩
 * 描述:属性动画工具类
 * 时间: 2016/10/9.
 */
public class AnimatorUtils {

    public static final String ROTATION = "rotation";
    public static final String SCALE = "scale";
    public static final String SCALE_X = "scaleX";
    public static final String SCALE_Y = "scaleY";
    public static final String TRANSLATION = "translation";
    public static final String TRANSLATION_X = "translationX";
    public static final String TRANSLATION_Y = "translationY";

    private AnimatorUtils() {
        //No instances.
    }

    public static PropertyValuesHolder rotation(float... values) {
        return PropertyValuesHolder.ofFloat(ROTATION, values);
    }

    public static PropertyValuesHolder translation(float... values) {
        return PropertyValuesHolder.ofFloat(TRANSLATION, values);
    }

    public static PropertyValuesHolder translationX(float... values) {
        return PropertyValuesHolder.ofFloat(TRANSLATION_X, values);
    }

    public static PropertyValuesHolder translationY(float... values) {
        return PropertyValuesHolder.ofFloat(TRANSLATION_Y, values);
    }

    public static PropertyValuesHolder scale(float... values) {
        return PropertyValuesHolder.ofFloat(SCALE, values);
    }

    public static PropertyValuesHolder scaleX(float... values) {
        return PropertyValuesHolder.ofFloat(SCALE_X, values);
    }

    public static PropertyValuesHolder scaleY(float... values) {
        return PropertyValuesHolder.ofFloat(SCALE_Y, values);
    }


    public static ObjectAnimator tada(View view) {
        return tada(view, 1f);
    }

    public static ObjectAnimator tada(View view, float shakeFactor) {

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(
                View.SCALE_X, Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f), Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f), Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f), Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f), Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f), Keyframe.ofFloat(1f, 1f));

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(
                View.SCALE_Y, Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f), Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f), Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f), Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f), Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f), Keyframe.ofFloat(1f, 1f));

        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(
                View.ROTATION, Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -3f * shakeFactor),
                Keyframe.ofFloat(.2f, -3f * shakeFactor),
                Keyframe.ofFloat(.3f, 3f * shakeFactor),
                Keyframe.ofFloat(.4f, -3f * shakeFactor),
                Keyframe.ofFloat(.5f, 3f * shakeFactor),
                Keyframe.ofFloat(.6f, -3f * shakeFactor),
                Keyframe.ofFloat(.7f, 3f * shakeFactor),
                Keyframe.ofFloat(.8f, -3f * shakeFactor),
                Keyframe.ofFloat(.9f, 3f * shakeFactor),
                Keyframe.ofFloat(1f, 0));

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX,
                pvhScaleY, pvhRotate).setDuration(1000);
    }

    public static ObjectAnimator nope(View view) {
        int delta = 40;
        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(
                View.TRANSLATION_X, Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.10f, -delta), Keyframe.ofFloat(.26f, delta),
                Keyframe.ofFloat(.42f, -delta), Keyframe.ofFloat(.58f, delta),
                Keyframe.ofFloat(.74f, -delta), Keyframe.ofFloat(.90f, delta),
                Keyframe.ofFloat(1f, 0f));

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX)
                .setDuration(500);
    }

    /**
     * 抖动动画
     */
    public static void startShakeAnim(Context context, View view) {
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        view.startAnimation(shake);
    }

}
