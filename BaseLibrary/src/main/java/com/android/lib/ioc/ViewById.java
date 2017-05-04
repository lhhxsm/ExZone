package com.android.lib.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者:李鸿浩
 * 描述:View注解的Annotation
 * 时间:2017/5/4.
 */
//@Target代表Annotation的位置
// 1.FIELD 属性
// 2.TYPE 类
// 3.METHOD 方法
// 4.CONSTRUCTOR 构造函数
//@Retention 什么时候生效
//1.CLASS 编译时
// 2.RUNTIME 运行时
// 3.SOURCE 源码资源
@Target(ElementType.FIELD) @Retention(RetentionPolicy.RUNTIME) public @interface ViewById {
  //@ViewById(@R.id.xxx)
  int value();
}
