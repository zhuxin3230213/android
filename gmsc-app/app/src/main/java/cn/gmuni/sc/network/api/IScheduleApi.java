package cn.gmuni.sc.network.api;

import java.util.List;
import java.util.Map;

import cn.gmuni.sc.network.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface IScheduleApi {
    /**
     * 获取课程表信息
     * @param params
     * @return
     */
    @POST("/api/schedule/getAppSchedule")
    Observable<BaseResponse<String>> getAppSchedule(@Body Map<String, String> params);

    /**
     * 获取学期信息
     * @param params
     * @return
     */
    @POST("/api/schedule/getScheduleInfo")
    Observable<BaseResponse<String>> getScheduleInfo(@Body Map<String, String> params);

    /**
     * 获取课程周数信息
     * @param params
     * @return
     */
    @POST("/api/schedule/getScheduleWeek")
    Observable<BaseResponse<String>> getScheduleWeek(@Body Map<String, String> params);
}
