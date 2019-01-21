package cn.gmuni.sc.network.api;

import cn.gmuni.sc.model.entities.UserInfoEntity;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

import java.util.List;
import java.util.Map;


import cn.gmuni.sc.model.CollegeModel;
import cn.gmuni.sc.network.BaseResponse;
import retrofit2.http.GET;


/**
 * 选择学校接口
 */
public interface IUserInfoApi {

    //获取所有的大学
    @GET("/bg/college/listAll")
    Observable<BaseResponse<List<CollegeModel>>> listAll();

    @POST("/thirdPartUser/updateUser")
    Observable<BaseResponse<Map<String,Object>>> updateUser(@Body UserInfoEntity entity);
}
