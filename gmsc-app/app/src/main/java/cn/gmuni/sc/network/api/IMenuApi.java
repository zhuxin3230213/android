package cn.gmuni.sc.network.api;

import java.util.Map;

import cn.gmuni.sc.network.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface IMenuApi {
    /**
     * 获取菜单项
     * @param params
     * @return
     */
    @POST("/menu/getItems")
    Observable<BaseResponse<Map<String, Object>>> getItems(@Body Map<String, String> params);

}
