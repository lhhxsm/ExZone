package com.exzone.lib.widget.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * ============================================================
 * 描 述 :
 * 作 者 : 鸿浩
 * 时 间 : 2015/12/17.
 * ============================================================
 */
public abstract class DefaultAdapter<Data> extends RecyclerView.Adapter<BaseViewHolder> {

  private List<Data> mDatas;
  private Context mContext;

  public DefaultAdapter(Context context) {
    init(context, new ArrayList<Data>());
  }

  public DefaultAdapter(Context context, Data[] datas) {
    init(context, Arrays.asList(datas));
  }

  public DefaultAdapter(Context context, List<Data> datas) {
    init(context, datas);
  }

  private void init(Context context, List<Data> datas) {
    this.mContext = context;
    this.mDatas = datas;
  }

  @Override public int getItemCount() {
    return mDatas != null ? mDatas.size() : 0;
  }

  @Override public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return onCreate(parent, viewType);
  }

  protected abstract BaseViewHolder onCreate(ViewGroup parent, int viewType);

  @Override public void onBindViewHolder(BaseViewHolder holder, int position) {
    Data data = getItem(position);
    holder.setData(data);
  }

  public Data getItem(int position) {
    return mDatas.get(position);
  }

  //============== 数据的增删改====================//
  public void add(Data data) {
    if (data != null) {
      mDatas.add(data);
    }
    notifyDataSetChanged();
  }

  public void addAll(Collection<? extends Data> collection) {
    if (collection != null && collection.size() != 0) {
      mDatas.addAll(collection);
    }
    notifyDataSetChanged();
  }

  public void addAll(Data... datas) {
    if (datas != null && datas.length != 0) {
      Collections.addAll(mDatas, datas);
    }
    notifyDataSetChanged();
  }

  /**
   * 在指定位置添加数据条目
   */
  public void insert(int location, Data data) {
    mDatas.add(location, data);
    notifyItemInserted(location);
  }

  /**
   * 删除指定索引数据条目
   */
  public void remove(int position) {
    mDatas.remove(position);
    notifyItemRemoved(position);
  }

  /**
   * 删除指定数据条目
   */
  public void remove(Data data) {
    remove(mDatas.indexOf(data));
  }

  /**
   * 清空数据列表
   */
  public void clear() {
    mDatas.clear();
    notifyDataSetChanged();
  }

  /**
   * 排序
   */
  public void sort(Comparator<? super Data> comparator) {
    Collections.sort(mDatas, comparator);
    notifyDataSetChanged();
  }

  /**
   * 获取数据集合
   */
  public List<Data> getDatas() {
    return mDatas;
  }

  /**
   * 设置全新的数据,如果传人null,则清空数据列表
   */
  public void setDatas(List<Data> datas) {
    if (datas != null) {
      mDatas = datas;
    } else {
      mDatas.clear();
    }
    notifyDataSetChanged();
  }

  /**
   * 在集合头部添加新的数据集合(下拉刷新)
   */
  public void addDatas(List<Data> datas) {
    if (datas != null) {
      mDatas.addAll(0, datas);
      notifyItemRangeInserted(0, datas.size());
    }
  }

  /**
   * 在集合尾部添加更多数据集合(上拉加载)
   */
  public void addMoreDatas(List<Data> datas) {
    if (datas != null) {
      mDatas.addAll(mDatas.size(), datas);
      notifyItemRangeInserted(mDatas.size(), datas.size());
    }
  }

  /**
   * 在集合头部添加数据条目
   */
  public void addFirst(Data data) {
    insert(0, data);
  }

  /**
   * 在集合末尾添加数据条目
   */
  public void addLast(Data data) {
    insert(mDatas.size(), data);
  }

  /**
   * 替换指定索引的数据条目
   */
  public void replace(int location, Data data) {
    mDatas.set(location, data);
    notifyItemChanged(location);
  }

  /**
   * 替换指定数据条目
   */
  public void replace(Data oldData, Data newData) {
    replace(mDatas.indexOf(oldData), newData);
  }

  /**
   * 移动数据条目的位置
   */
  public void move(int fromPosition, int toPosition) {
    mDatas.add(toPosition, mDatas.remove(fromPosition));
    notifyItemMoved(fromPosition, toPosition);
  }

  ///////////////////////////////////////////////////////
}
