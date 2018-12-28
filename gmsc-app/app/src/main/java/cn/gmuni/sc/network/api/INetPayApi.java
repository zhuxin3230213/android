package cn.gmuni.sc.network.api;

import java.util.Map;

import cn.gmuni.sc.network.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface INetPayApi {
    /**
     * 查询用户上网账号是否存在
     * @param params
     * @return
     */
    @POST("/networkPay/checkNetAccountExist")
    Observable<BaseResponse<Map<String, String>>> checkNetAccountExist(@Body Map<String, String> params);

    /**
     * 获取用户上网账号信息
     * @param params
     * @return
    */
    @POST("/networkPay/getAccountInfo")
    Observable<BaseResponse<Object>> getAccountInfo(@Body Map<String, String> params);

}
