package com.exzone.lib.fragment;

import android.support.v4.app.Fragment;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间: 2017/2/8.
 */
public interface FragmentNavigatorAdapter {

    public Fragment onCreateFragment(int position);

    public String getTag(int position);

    public int getCount();
}