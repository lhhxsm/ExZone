package com.exzone.lib.rxjava;

import com.exzone.lib.entity.UpdateEntity;
import retrofit2.http.GET;
import rx.Observable;

/**
 * 作者:lhh
 * 描述:
 * 时间:2016/7/6.
 */
public interface APIService {
  String ENDPOINT = "https://......";

  // 查询app更新信息
  @GET("......") Observable<UpdateEntity> geUpdateInfo();
}
