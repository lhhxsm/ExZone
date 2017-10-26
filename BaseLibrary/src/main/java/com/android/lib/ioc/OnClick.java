package com.android.lib.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者:lhh
 * 描述:View事件注解的Annotation
 * 时间:2017/5/4.
 */
@Target(ElementType.METHOD) @Retention(RetentionPolicy.RUNTIME) public @interface OnClick {
  int[] value();
}
