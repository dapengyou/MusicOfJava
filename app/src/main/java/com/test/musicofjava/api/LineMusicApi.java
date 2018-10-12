package com.test.musicofjava.api;

import com.test.musicofjava.bean.Address;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @createTime: 2018/10/10
 * @author: lady_zhou
 * @Description: 线上音乐api
 */
public interface LineMusicApi {
    @GET("book/search")
    Call<Address> getString(@Query("q") String name,
                            @Query("tag") String tag,
                            @Query("start") int start,
                            @Query("count") int count);
}
