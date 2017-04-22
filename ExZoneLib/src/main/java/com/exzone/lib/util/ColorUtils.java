package com.exzone.lib.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.exzone.lib.R;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 作者:李鸿浩
 * 描述:颜色生成器
 * 时间:2016/7/9.
 */
public class ColorUtils {
    /**
     * 白色
     */
    public static final int WHITE = 0xffffffff;
    /**
     * 白色 - 半透明
     */
    public static final int WHITE_TRANSLUCENT = 0x80ffffff;
    /**
     * 黑色
     */
    public static final int BLACK = 0xff000000;
    /**
     * 黑色 - 半透明
     */
    public static final int BLACK_TRANSLUCENT = 0x80000000;
    /**
     * 透明
     */
    public static final int TRANSPARENT = 0x00000000;
    /**
     * 红色
     */
    public static final int RED = 0xffff0000;
    /**
     * 红色 - 半透明
     */
    public static final int RED_TRANSLUCENT = 0x80ff0000;
    /**
     * 红色 - 深的
     */
    public static final int RED_DARK = 0xff8b0000;
    /**
     * 红色 - 深的 - 半透明
     */
    public static final int RED_DARK_TRANSLUCENT = 0x808b0000;
    /**
     * 绿色
     */
    public static final int GREEN = 0xff00ff00;
    /**
     * 绿色 - 半透明
     */
    public static final int GREEN_TRANSLUCENT = 0x8000ff00;
    /**
     * 绿色 - 深的
     */
    public static final int GREEN_DARK = 0xff003300;
    /**
     * 绿色 - 深的 - 半透明
     */
    public static final int GREEN_DARK_TRANSLUCENT = 0x80003300;
    /**
     * 绿色 - 浅的
     */
    public static final int GREEN_LIGHT = 0xffccffcc;
    /**
     * 绿色 - 浅的 - 半透明
     */
    public static final int GREEN_LIGHT_TRANSLUCENT = 0x80ccffcc;
    /**
     * 蓝色
     */
    public static final int BLUE = 0xff0000ff;
    /**
     * 蓝色 - 半透明
     */
    public static final int BLUE_TRANSLUCENT = 0x800000ff;
    /**
     * 蓝色 - 深的
     */
    public static final int BLUE_DARK = 0xff00008b;
    /**
     * 蓝色 - 深的 - 半透明
     */
    public static final int BLUE_DARK_TRANSLUCENT = 0x8000008b;
    /**
     * 蓝色 - 浅的
     */
    public static final int BLUE_LIGHT = 0xff36a5E3;
    /**
     * 蓝色 - 浅的 - 半透明
     */
    public static final int BLUE_LIGHT_TRANSLUCENT = 0x8036a5E3;
    /**
     * 天蓝
     */
    public static final int SKY_BLUE = 0xff87ceeb;
    /**
     * 天蓝 - 半透明
     */
    public static final int SKY_BLUE_TRANSLUCENT = 0x8087ceeb;
    /**
     * 天蓝 - 深的
     */
    public static final int SKY_BLUE_DARK = 0xff00bfff;
    /**
     * 天蓝 - 深的 - 半透明
     */
    public static final int SKY_BLUE_DARK_TRANSLUCENT = 0x8000bfff;
    /**
     * 天蓝 - 浅的
     */
    public static final int SKY_BLUE_LIGHT = 0xff87cefa;
    /**
     * 天蓝 - 浅的 - 半透明
     */
    public static final int SKY_BLUE_LIGHT_TRANSLUCENT = 0x8087cefa;
    /**
     * 灰色
     */
    public static final int GRAY = 0xff969696;
    /**
     * 灰色 - 半透明
     */
    public static final int GRAY_TRANSLUCENT = 0x80969696;
    /**
     * 灰色 - 深的
     */
    public static final int GRAY_DARK = 0xffa9a9a9;
    /**
     * 灰色 - 深的 - 半透明
     */
    public static final int GRAY_DARK_TRANSLUCENT = 0x80a9a9a9;
    /**
     * 灰色 - 暗的
     */
    public static final int GRAY_DIM = 0xff696969;
    /**
     * 灰色 - 暗的 - 半透明
     */
    public static final int GRAY_DIM_TRANSLUCENT = 0x80696969;
    /**
     * 灰色 - 浅的
     */
    public static final int GRAY_LIGHT = 0xffd3d3d3;
    /**
     * 灰色 - 浅的 - 半透明
     */
    public static final int GRAY_LIGHT_TRANSLUCENT = 0x80d3d3d3;
    /**
     * 橙色
     */
    public static final int ORANGE = 0xffffa500;
    /**
     * 橙色 - 半透明
     */
    public static final int ORANGE_TRANSLUCENT = 0x80ffa500;
    /**
     * 橙色 - 深的
     */
    public static final int ORANGE_DARK = 0xffff8800;
    /**
     * 橙色 - 深的 - 半透明
     */
    public static final int ORANGE_DARK_TRANSLUCENT = 0x80ff8800;
    /**
     * 橙色 - 浅的
     */
    public static final int ORANGE_LIGHT = 0xffffbb33;
    /**
     * 橙色 - 浅的 - 半透明
     */
    public static final int ORANGE_LIGHT_TRANSLUCENT = 0x80ffbb33;
    /**
     * 金色
     */
    public static final int GOLD = 0xffffd700;
    /**
     * 金色 - 半透明
     */
    public static final int GOLD_TRANSLUCENT = 0x80ffd700;
    /**
     * 粉色
     */
    public static final int PINK = 0xffffc0cb;
    /**
     * 粉色 - 半透明
     */
    public static final int PINK_TRANSLUCENT = 0x80ffc0cb;
    /**
     * 紫红色
     */
    public static final int FUCHSIA = 0xffff00ff;
    /**
     * 紫红色 - 半透明
     */
    public static final int FUCHSIA_TRANSLUCENT = 0x80ff00ff;
    /**
     * 灰白色
     */
    public static final int GRAY_WHITE = 0xfff2f2f2;
    /**
     * 灰白色 - 半透明
     */
    public static final int GRAY_WHITE_TRANSLUCENT = 0x80f2f2f2;
    /**
     * 紫色
     */
    public static final int PURPLE = 0xff800080;
    /**
     * 紫色 - 半透明
     */
    public static final int PURPLE_TRANSLUCENT = 0x80800080;
    /**
     * 青色
     */
    public static final int CYAN = 0xff00ffff;
    /**
     * 青色 - 半透明
     */
    public static final int CYAN_TRANSLUCENT = 0x8000ffff;
    /**
     * 青色 - 深的
     */
    public static final int CYAN_DARK = 0xff008b8b;
    /**
     * 青色 - 深的 - 半透明
     */
    public static final int CYAN_DARK_TRANSLUCENT = 0x80008b8b;
    /**
     * 黄色
     */
    public static final int YELLOW = 0xffffff00;
    /**
     * 黄色 - 半透明
     */
    public static final int YELLOW_TRANSLUCENT = 0x80ffff00;
    /**
     * 黄色 - 浅的
     */
    public static final int YELLOW_LIGHT = 0xffffffe0;
    /**
     * 黄色 - 浅的 - 半透明
     */
    public static final int YELLOW_LIGHT_TRANSLUCENT = 0x80ffffe0;
    /**
     * 巧克力色
     */
    public static final int CHOCOLATE = 0xffd2691e;
    /**
     * 巧克力色 - 半透明
     */
    public static final int CHOCOLATE_TRANSLUCENT = 0x80d2691e;
    /**
     * 番茄色
     */
    public static final int TOMATO = 0xffff6347;
    /**
     * 番茄色 - 半透明
     */
    public static final int TOMATO_TRANSLUCENT = 0x80ff6347;
    /**
     * 橙红色
     */
    public static final int ORANGE_RED = 0xffff4500;
    /**
     * 橙红色 - 半透明
     */
    public static final int ORANGE_RED_TRANSLUCENT = 0x80ff4500;
    /**
     * 银白色
     */
    public static final int SILVER = 0xffc0c0c0;
    /**
     * 银白色 - 半透明
     */
    public static final int SILVER_TRANSLUCENT = 0x80c0c0c0;
    /**
     * 高光
     */
    public static final int HIGH_LIGHT = 0x33ffffff;
    /**
     * 低光
     */
    public static final int LOW_LIGHT = 0x33000000;
    private final static float LIGHTNESS_THRESHOLD = 0.72f;
    public static ColorUtils DEFAULT;
    public static ColorUtils MATERIAL;

    static {
        DEFAULT = create(Arrays.asList(
                0xfff16364,
                0xfff58559,
                0xfff9a43e,
                0xffe4c62e,
                0xff67bf74,
                0xff59a2be,
                0xff2093cd,
                0xffad62a7,
                0xff805781
        ));
        MATERIAL = create(Arrays.asList(
                0xffe57373,
                0xfff06292,
                0xffba68c8,
                0xff9575cd,
                0xff7986cb,
                0xff64b5f6,
                0xff4fc3f7,
                0xff4dd0e1,
                0xff4db6ac,
                0xff81c784,
                0xffaed581,
                0xffff8a65,
                0xffd4e157,
                0xffffd54f,
                0xffffb74d,
                0xffa1887f,
                0xff90a4ae
        ));
    }

    private final List<Integer> mColors;
    private final Random mRandom;

    private ColorUtils(List<Integer> colorList) {
        mColors = colorList;
        mRandom = new Random(System.currentTimeMillis());
    }

    public static ColorUtils create(List<Integer> colorList) {
        return new ColorUtils(colorList);
    }

    /**
     * Set the alpha portion of the color.
     *
     * @param color the (a)rgb color to set an alpha for.
     * @param alpha the new alpha value, between 0 and 255.
     */
    public static
    @ColorInt
    int setAlpha(final int color, @IntRange(from = 0, to = 255) final int alpha) {
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    public static
    @ColorInt
    int darkColor(final Context context) {
        return ContextCompat.getColor(context, darkColorId());
    }

    public static
    @ColorRes
    int darkColorId() {
        return R.color.text_dark;
    }

    /**
     * Darken the argb color by a percentage.
     *
     * @param color   the argb color to lighten.
     * @param percent percentage to darken by, between 0.0 and 1.0.
     */
    public static
    @ColorInt
    int darken(@ColorInt final int color, @FloatRange(from = 0.0, to = 1.0) final float percent) {
        final float[] hsl = new float[3];
        android.support.v4.graphics.ColorUtils.colorToHSL(color, hsl);
        hsl[2] -= hsl[2] * percent;
        // HSLToColor sets alpha to fully opaque, so pluck the alpha from the original color.
        return (color & 0xFF000000) | (android.support.v4.graphics.ColorUtils.HSLToColor(hsl) & 0x00FFFFFF);
    }

    public static
    @ColorInt
    int lightColor(final Context context) {
        return ContextCompat.getColor(context, lightColorId());
    }

    public static
    @ColorRes
    int lightColorId() {
        return R.color.white;
    }

    /**
     * Lighten the argb color by a percentage.
     *
     * @param color   the argb color to lighten.
     * @param percent percentage to lighten by, between 0.0 and 1.0.
     */
    public static
    @ColorInt
    int lighten(@ColorInt final int color, @FloatRange(from = 0.0, to = 1.0) final float percent) {
        final float[] hsl = new float[3];
        android.support.v4.graphics.ColorUtils.colorToHSL(color, hsl);
        hsl[2] += (1.0f - hsl[2]) * percent;
        // HSLToColor sets alpha to fully opaque, so pluck the alpha from the original color.
        return (color & 0xFF000000) | (android.support.v4.graphics.ColorUtils.HSLToColor(hsl) & 0x00FFFFFF);
    }

    /**
     * Check whether a color is light.
     *
     * @param color the argb color to check.
     */
    public static boolean isLight(@ColorInt final int color) {
        return weightedLightness(color) >= LIGHTNESS_THRESHOLD;
    }

    /**
     * Check whether a color is dark.
     *
     * @param color the argb color to check.
     */
    public static boolean isDark(@ColorInt final int color) {
        return !isLight(color);
    }

    public static
    @ColorInt
    int foregroundColor(final int backgroundColor, final @NonNull Context context) {
        final @ColorRes int colorId = isLight(backgroundColor) ? darkColorId() : lightColorId();
        return ContextCompat.getColor(context, colorId);
    }

    /*
     * Return a value between 0.0 and 1.0 representing the perceived lightness of the color.
     * More info here: https://robots.thoughtbot.com/closer-look-color-lightness
     */
    private static double weightedLightness(@ColorInt final int color) {
        return ((Color.red(color) * 212.6 + Color.green(color) * 715.2 + Color.blue(color) * 72.2) / 1000) / 255;
    }

    public int getRandomColor() {
        return mColors.get(mRandom.nextInt(mColors.size()));
    }

    public int getColor(Object key) {
        return mColors.get(Math.abs(key.hashCode()) % mColors.size());
    }

}
