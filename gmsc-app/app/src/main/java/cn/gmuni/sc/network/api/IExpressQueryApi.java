package cn.gmuni.sc.network.api;

import java.util.List;
import java.util.Map;

import cn.gmuni.sc.model.express.CompanyModel;
import cn.gmuni.sc.network.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @Author: zhuxin
 * @Date: 2018/9/17 15:35
 * @Description:
 */
public interface IExpressQueryApi {

    @POST("/express/company")
    Observable<BaseResponse<List<Map<String,String>>>> expressByExpNo(@Body Map<String,String> params);

    @POST("/express/information")
    Observable<BaseResponse<List<Map<String,String>>>> expressQuery(@Body Map<String,String> params);

    @POST("/express/query")
    Observable<BaseResponse<List<Map<String,String>>>>  expressByExpNoWithOneH(@Body Map<String,String> params);
}
