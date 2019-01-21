package cn.gmuni.sc.network.api;


import java.util.List;
import java.util.Map;

import cn.gmuni.sc.model.MarketModel;
import cn.gmuni.sc.network.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @Author:ZhuXin
 * @Description:
 * @Date:Create 2019/1/10 11:40
 * @Modified By:
 **/
public interface IMarketApi {

    @POST("/market/add")
    Observable<BaseResponse<MarketModel>> add(@Body MarketModel marketModel);
    @POST("/market/del")
    Observable<BaseResponse<Map<String,Object>>> delete(@Body Map<String,String> params);
    @POST("/market/update")
    Observable<BaseResponse<MarketModel>> update(@Body MarketModel marketModel);
    @POST("/market/updateSaleStatus")
    Observable<BaseResponse<Map<String,Object>>> updateSaleStatus(@Body Map<String ,String> params);
    @POST("/market/list")
    Observable<BaseResponse<List<MarketModel>>> listMarket(@Body Map<String, String> params);
    @POST("/market/getMarketById")
    Observable<BaseResponse<MarketModel>> findById(@Body Map<String, String> params);

    @POST("/report/add")
    Observable<BaseResponse<Map<String,Object>>> addReport(@Body Map<String,String> params);
    @POST("/leave/add")
    Observable<BaseResponse<Map<String,Object>>> addMessage(@Body Map<String,String> params);
    @POST("/leave/list")
    Observable<BaseResponse<List<Map<String,Object>>>> listMessage(@Body Map<String,String> params);
}
