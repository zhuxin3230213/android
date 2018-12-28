package cn.gmuni.sc.network.api;

import java.util.Date;
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
 * @Date:Create in 14:22 2018/8/20
 * @Modified By:
 **/
public interface INewsApi {

    @GET("/api/news/listNews")
    Observable<BaseResponse<List<Map<String,String>>>> listNews(@QueryMap Map<String, String> params);

    @GET("/api/news/addNewsTop")
    Observable<BaseResponse<List<Map<String,String>>>> addNewsTop(@QueryMap Map<String,String> params);

    @GET("/api/news/addNewsBottom")
    Observable<BaseResponse<List<Map<String,String>>>> addNewsBottom(@QueryMap Map<String,String> params);

    @GET("/api/news/lastNew")
    Observable<BaseResponse<Map<String,String>>> lastNew();

    @GET("/api/click/updateClickThrough")
    Observable<BaseResponse<Map<String,String>>> updateClickThrough(@QueryMap Map<String,String> params);

    @POST("/api/news/getNew")
    Observable<BaseResponse<Map<String,Object>>>  getNew(@Body Map<String, String> params);

}
