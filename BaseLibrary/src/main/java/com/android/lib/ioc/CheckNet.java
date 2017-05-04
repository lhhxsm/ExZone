package com.android.lib.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者:李鸿浩
 * 描述:检测网络
 * 时间:2017/5/4.
 */
@Target(ElementType.METHOD) @Retention(RetentionPolicy.RUNTIME) public @interface CheckNet {
}
