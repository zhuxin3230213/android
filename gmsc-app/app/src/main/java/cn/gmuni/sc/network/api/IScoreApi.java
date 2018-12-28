package cn.gmuni.sc.network.api;

import java.util.Map;

import cn.gmuni.sc.model.CetScoreModel;
import cn.gmuni.sc.model.ComputerScoreModel;
import cn.gmuni.sc.model.PutonghuaModel;
import cn.gmuni.sc.network.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface IScoreApi {

    @POST("/score/getPutonghuaScore")
    Observable<BaseResponse<PutonghuaModel>> getPutonghuaScore(@Body Map<String,String> map);

    @GET("/score/initComputer")
    Observable<BaseResponse<Map<String,Object>>> initComputer();

    @POST("/score/listComputerSubject")
    Observable<BaseResponse<Map<String,Object>>> listComputerSubject(@Body Map<String,String> params);

    @POST("/score/getCumputerScore")
    Observable<BaseResponse<ComputerScoreModel>> getCumputerScore(@Body Map<String,String> params);


    @GET("/score/initCet")
    Observable<BaseResponse<Map<String,String>>> initCet();

    @POST("/score/getCetScore")
    Observable<BaseResponse<CetScoreModel>> getCetScore(@Body Map<String,String> params);
}
