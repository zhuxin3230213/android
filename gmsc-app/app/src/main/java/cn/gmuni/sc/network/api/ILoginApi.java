package cn.gmuni.sc.network.api;

import java.util.Map;

import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.network.BaseResponse;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ILoginApi {

    @FormUrlEncoded
    @POST("/sendSms")
    Observable<BaseResponse<Map<String,Object>>> sendSms(@Field("phoneNumber") String phoneNumber);

    @FormUrlEncoded
    @POST("/loginSms")
    Observable<BaseResponse<Map<String,Object>>> loginSms(@Field("phoneNumber") String phoneNumber,@Field("verificationCode") String verificationCode,@Field("deviceUniqueId") String deviceUniqueId);


    @POST("/logout")
    Observable<BaseResponse<Map<String,Object>>> logout();

    @POST("/endpoint/login")
    Observable<BaseResponse<Map<String,Object>>>loginEndPoint(@Body Map<String,String> params);
}
