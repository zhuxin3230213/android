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
 * @Date:Create 2018/11/21 14:58
 * @Modified By:
 **/
public interface IMessageApi {

    //获取消息列表数据
    @POST("/message/listMessage")
    Observable<BaseResponse<List<Map<String,Object>>>> listMessage(@Body Map<String, String> params);

    //消息点击后状态更改
    @POST("/message/updateState")
    Observable<BaseResponse<Map<String,Object>>> updateByIdAndType(@Body Map<String,String> params);

    @POST("/message/addMessageTop")
    Observable<BaseResponse<List<Map<String,Object>>>> addMessageTop(@Body Map<String,String> params);

    @POST("/message/addMessageBottom")
    Observable<BaseResponse<List<Map<String,Object>>>> addMessageBottom(@Body Map<String,String> params);

    @POST("/message/getMessageState")
    Observable<BaseResponse<Map<String,Object>>> getMessageState();
}
