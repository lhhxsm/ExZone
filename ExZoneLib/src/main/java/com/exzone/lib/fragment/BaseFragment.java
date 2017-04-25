package com.exzone.lib.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2016/7/5.
 */
public abstract class BaseFragment extends Fragment {
  protected View mRootView;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mRootView = inflater.inflate(layoutResID(), container, false);
    return mRootView;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    onViewCreated(savedInstanceState);
  }

  @Override public void onDetach() {
    super.onDetach();
  }

  protected abstract int layoutResID();

  protected abstract void onViewCreated(Bundle savedInstanceState);

  protected void fadeIn(View view) {
    Animation bottomUp = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
    view.startAnimation(bottomUp);
    view.setVisibility(View.VISIBLE);
  }

  protected void fadeOut(View view) {
    Animation bottomUp = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
    view.startAnimation(bottomUp);
    view.setVisibility(View.GONE);
  }
}
