package cn.gmuni.sc.network.api;

import java.util.List;
import java.util.Map;

import cn.gmuni.sc.network.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @Author: zhuxin
 * @Date: 2018/9/4 09:32
 * @Description:
 */
public interface INoticeApi {

    @GET("/api/notice/listNotice")
    Observable<BaseResponse<List<Map<String,String>>>> listNotices(@QueryMap Map<String, String> params);

    @GET("/api/notice/addNoticeTop")
    Observable<BaseResponse<List<Map<String,String>>>> addNoticeTop(@QueryMap Map<String,String> params);

    @GET("/api/notice/addNoticeBottom")
    Observable<BaseResponse<List<Map<String,String>>>> addNoticeBottom(@QueryMap Map<String,String> params);

    @GET("/api/notice/lastNotice")
    Observable<BaseResponse<Map<String,String>>> lastNew();

    @GET(value = "/api/notice/getNotice")
    Observable<BaseResponse<Map<String,Object>>>  getNotice(@Body Map<String, String> params);
}
