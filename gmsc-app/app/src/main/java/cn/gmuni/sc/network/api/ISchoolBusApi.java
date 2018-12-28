package cn.gmuni.sc.network.api;

import java.util.List;
import java.util.Map;

import cn.gmuni.sc.network.BaseResponse;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @Author:ZhuXin
 * @Description:
 * @Date:Create 2018/12/26 15:26
 * @Modified By:
 **/
public interface ISchoolBusApi {

    @GET("/api/schedbus/check")
    Observable<BaseResponse<Map<String, String>>> getCheckSeasonType();

    @GET("/api/schedbus/time")
    Observable<BaseResponse<Map<String, String>>> getSchoolBusTimeList(@QueryMap Map<String, String> map);

    @GET("/api/schedbus/campusList")
    Observable<BaseResponse<List<Map<String,String>>>> getSchoolCampusList();
}
