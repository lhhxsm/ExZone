package com.exzone.lib.widget.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * ============================================================
 * 描 述 :
 * 作 者 : 鸿浩
 * 时 间 : 2015/12/9.
 * ============================================================
 */
public abstract class BaseViewHolder<Data> extends RecyclerView.ViewHolder {
    private Data data;

    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public BaseViewHolder(ViewGroup parent, @LayoutRes int res) {
        super(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));
        ButterKnife.bind(this, itemView);
    }

    public void setData(Data data) {
        this.data = data;
        refreshView(data);
    }


    /**
     * 根据数据刷新界面
     */
    protected abstract void refreshView(Data data);
}
