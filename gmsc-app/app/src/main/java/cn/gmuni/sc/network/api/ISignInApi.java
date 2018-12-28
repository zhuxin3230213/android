package cn.gmuni.sc.network.api;


import android.app.MediaRouteActionProvider;

import java.util.List;
import java.util.Map;

import cn.gmuni.sc.network.BaseResponse;
import cn.gmuni.sc.utils.MToast;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @Author: zhuxin
 * @Date: 2018/11/1 14:44
 * @Description:
 */
public interface ISignInApi {

    @POST("/integral/task")
    Observable<BaseResponse<Map<String,Object>>> signIn(@Body Map<String,String> params); //获取签到积分详情

    @POST("/integral/StatisticsByTaskcode")
    Observable<BaseResponse<Map<String,Object>>> findIntegralStatisticsByTakcodeAndUserInfo(@Body Map<String,String> params); //根据任务编码、用户去积分统计表中查询此对象

    @POST("/integral/findTaskByCode")
    Observable<BaseResponse<Map<String,Object>>> findTaskByCode(@Body Map<String,String> params); //根就任务编码获取任务积分integral

    @POST("/integral/listByTaskCode")
    Observable<BaseResponse<Map<String,Object>>> listByTaskCode(@Body Map<String,String> params);//根据任务编码\用户、学校与年月日查询当天状态

    @POST("/integral/listByMonth")
    Observable<BaseResponse<List<Map<String,Object>>>> listByMonth(@Body Map<String,String> params);//根据任务编码、用户、学校、年月查询本月列表签到状态
}
