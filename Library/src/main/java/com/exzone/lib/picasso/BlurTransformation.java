package com.exzone.lib.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.squareup.picasso.Transformation;

/**
 * 作者:李鸿浩
 * 描述:模糊一张图片(API必须大于或者等于17才有效)
 * 时间：2016/10/17.
 */
public class BlurTransformation implements Transformation {
    RenderScript mRenderScript;

    public BlurTransformation(Context context) {
        super();
        mRenderScript = RenderScript.create(context);
    }

    @Override
    public Bitmap transform(Bitmap source) {
        // 创建一个Bitmap作为最后处理的效果Bitmap
        Bitmap blurredBitmap = source.copy(Bitmap.Config.ARGB_8888, true);
        // 分配内存
        Allocation input = Allocation.createFromBitmap(mRenderScript, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
        Allocation output = Allocation.createTyped(mRenderScript, input.getType());
        // 根据我们想使用的配置加载一个实例
        ScriptIntrinsicBlur script = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            script = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));
            script.setInput(input);
            // 设置模糊半径
            script.setRadius(10);
            //开始操作
            script.forEach(output);
            // 将结果copy到blurredBitmap中
            output.copyTo(blurredBitmap);
        }
        //释放资源
        source.recycle();
        return blurredBitmap;
    }

    @Override
    public String key() {
        return "blur";
    }
}
