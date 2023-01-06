package com.hotbitmapgg.moequest.network.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DoubanMeizhiApi {

  /**
   * 根据cid查询不同类型的妹子图片
   */
  @GET("show.htm")
  Call<ResponseBody> getDoubanMeizi(@Query("cid") int cid, @Query("pager_offset") int pageNum);
}
