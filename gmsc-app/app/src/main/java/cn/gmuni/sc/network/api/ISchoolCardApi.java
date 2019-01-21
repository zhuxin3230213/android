package cn.gmuni.sc.network.api;

import java.util.Map;

import cn.gmuni.sc.network.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface ISchoolCardApi {
    /**
     * 获取用户钱包信息
     * @return
     */
    @POST("/wallet/getWallet")
    Observable<BaseResponse<Object>> getWallet();

    /**
     * 获取用户钱包信息
     * @return
     */
    @POST("/wallet/openCard")
    Observable<BaseResponse<Object>> openCard(@Body Map<String, String> params);
}
