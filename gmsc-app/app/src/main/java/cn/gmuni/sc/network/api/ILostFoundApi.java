package cn.gmuni.sc.network.api;

import java.util.List;
import java.util.Map;

import cn.gmuni.sc.model.LostFoundModel;
import cn.gmuni.sc.network.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface ILostFoundApi {
    @POST("/lostfound/add")
    Observable<BaseResponse<LostFoundModel>> add(@Body LostFoundModel info);
    @POST("/lostfound/delete")
    Observable<BaseResponse<Map<String, String>>> delete(@Body Map<String, String> params);
    @POST("/lostfound/finish")
    Observable<BaseResponse<Map<String, String>>> finish(@Body Map<String, String> params);
    @POST("/lostfound/edit")
    Observable<BaseResponse<Map<String, String>>> edit(@Body LostFoundModel info);
    @POST("/lostfound/list")
    Observable<BaseResponse<List<LostFoundModel>>> list(@Body Map<String, String> params);
    @POST("/lostfound/getOne")
    Observable<BaseResponse<LostFoundModel>> getOne(@Body Map<String, String> params);

}
