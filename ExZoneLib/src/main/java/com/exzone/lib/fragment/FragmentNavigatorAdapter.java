package com.exzone.lib.fragment;

import android.support.v4.app.Fragment;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间: 2017/2/8.
 */
public interface FragmentNavigatorAdapter {

  Fragment onCreateFragment(int position);

  String getTag(int position);

  int getCount();
}