package cn.gmuni.sc.network.api;

import java.util.List;
import java.util.Map;


import cn.gmuni.sc.network.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @Author:ZhuXin
 * @Description:
 * @Date:Create 2018/11/29 9:04
 * @Modified By:
 **/
public interface IGprsApi {

    @GET("/api/student/stuBehavior")
    Observable<BaseResponse<Map<String,Object>>>  sendGprsMessage(@QueryMap Map<String,String> params);

    @GET("/api/student/stuBehaviorList")
    Observable<BaseResponse<List<Map<String,Object>>>> list(@QueryMap Map<String,String> params);
}
