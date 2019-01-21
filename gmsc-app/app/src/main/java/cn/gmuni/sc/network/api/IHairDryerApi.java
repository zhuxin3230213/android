package cn.gmuni.sc.network.api;

import java.util.Map;

import cn.gmuni.sc.network.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @Author:ZhuXin
 * @Description:
 * @Date:Create 2018/12/7 10:03
 * @Modified By:
 **/
public interface IHairDryerApi {

    @POST("/blower/getBlowerStatus")
    Observable<BaseResponse<Map<String,Object>>>  getHairDryerStatus(@Body Map<String,String> params);
}
