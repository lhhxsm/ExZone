package com.android.lib.ioc;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;
import com.android.lib.utils.NetUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/5/4.
 */
public class ViewUtils {

  public static void inject(Activity activity) {
    inject(new ViewFinder(activity), activity);
  }

  public static void inject(View view) {
    inject(new ViewFinder(view), view);
  }

  public static void inject(View view, Object object) {
    inject(new ViewFinder(view), object);
  }

  /**
   * 兼容上面的三个方法
   *
   * @param finder findViewById的辅助类
   * @param object 反射执行的类
   */
  private static void inject(ViewFinder finder, Object object) {
    injectFiled(finder, object);//注入属性
    injectEvent(finder, object);//注入事件
  }

  /**
   * 注入属性
   */
  private static void injectFiled(ViewFinder finder, Object object) {
    //1.获取类里面的所有属性
    Class<?> clazz = object.getClass();
    //获取所有属性,包括公有属性和私有属性
    Field[] fields = clazz.getDeclaredFields();

    for (Field field : fields) {
      ViewById viewById = field.getAnnotation(ViewById.class);
      if (viewById != null) {

        //2.获取ViewById里面的value值
        //获取注解里面的id值-->R.id.xxx
        int value = viewById.value();

        //3.findViewById查找到view
        View view = finder.findViewById(value);
        if (view != null) {

          field.setAccessible(true); //表示能够注入所有的修饰符 private public ...

          //4.动态的注入到查找到的view
          try {
            field.set(object, view);
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  private static void injectEvent(ViewFinder finder, Object object) {
    //1.获取类里面的所有方法
    Class<?> clazz = object.getClass();
    Method[] methods = clazz.getDeclaredMethods();

    for (Method method : methods) {
      OnClick onClick = method.getAnnotation(OnClick.class);
      if (onClick != null) {

        //2.获取ViewById里面的value值
        int[] value = onClick.value();
        for (int viewId : value) {

          //3.findViewById查找到view
          View view = finder.findViewById(viewId);

          //扩展功能 检测网络
          boolean isCheckNet = method.getAnnotation(CheckNet.class) != null;

          if (view != null) {

            //4.view.setOnClickListener
            view.setOnClickListener(new DeclaredOnClickListener(method, object, isCheckNet));
          }
        }
      }
    }
  }

  private static class DeclaredOnClickListener implements View.OnClickListener {
    private Method mMethod;
    private Object mObject;
    private boolean isCheckNet;

    public DeclaredOnClickListener(Method method, Object object, boolean isCheckNet) {
      this.mMethod = method;
      this.mObject = object;
      this.isCheckNet = isCheckNet;
    }

    @Override public void onClick(View v) {
      //判断是否需要检测网络
      if (isCheckNet) {//需要
        //判断网络
        if (!NetUtil.isAvailable(v.getContext())) {
          Toast.makeText(v.getContext(), "亲,您的网络不太给力", Toast.LENGTH_SHORT).show();
          return;
        }
      }

      //点击会调用的方法
      try {
        mMethod.setAccessible(true); //表示能够注入所有的修饰符 private public ...

        //5.反射执行方法
        mMethod.invoke(mObject, v);
      } catch (Exception e) {
        e.printStackTrace();
        try {
          mMethod.invoke(mObject);//没有传入参数
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    }
  }
}
