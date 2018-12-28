package cn.gmuni.sc.network;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.config.Const;
import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.module.login.LoginActivity;
import cn.gmuni.sc.network.adapter.DateTypeAdapter;
import cn.gmuni.sc.network.adapter.GsonTypeAdapter;
import cn.gmuni.sc.umpush.UmPushHelper;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.NetUtils;
import cn.gmuni.sc.utils.SecurityUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Network {

//    //缓存文件最大限制20M
//    private static final long cacheSize = 1028*1024*20;
//
//    //设置缓存文件路径
//    private static String cacheDirectory = Environment.getExternalStorageDirectory() + "/gmsc/caches";
//
//    private  static Cache cache = new Cache(new File(cacheDirectory),cacheSize);


    static{

    }

    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create(buildGson());
    private static Gson gson = null;
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static Retrofit retrofit = null;

    /**
     * 传入Api类，
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T createApi(Class<T> t){
        if(retrofit==null){
            final String url = BaseApplication.getInstance().getSysConfig("server.url");
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(OkHttpClientHelper.getClient())
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
        }

        return retrofit.create(t);
    }

    /**
     * 请求数据
     * @param <T>
     * @param observable
     * @param listener
     * @return
     */
    public static <T> void request(Observable<BaseResponse<T>> observable, final IResponseListener<T> listener){
        if (!NetUtils.isConnected(BaseApplication.getInstance())) {
            Logger.e("网络不可用,请检查网络");
            if (listener != null) {
                listener.onFail(500,"网络不可用,请检查网络");
            }
            return;
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<T>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Logger.e(e.getMessage());
                        if (listener != null) {
                            listener.onFail(500, e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(BaseResponse<T> data) {
                        if (listener != null) {
                            if (data.isSuccess()) {
                                listener.onSuccess(data.getData());
                            } else {
                                //在报403错误时，跳转到登录页面
                                if (data.getCode() == 403) {
                                    MToast.showShortToast(data.getMessage());
                                    SecurityUtils.logout();
                                    //用户退出登录时将所有推送标签删除
                                    UmPushHelper.getInstance().removeAllTags();
                                    BaseApplication.getInstance().finishActivities();
                                    Intent intent = new Intent(BaseApplication.getInstance().getBaseContext(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    BaseApplication.getInstance().startActivity(intent);
                                } else {
                                    listener.onFail(data.getCode(), data.getMessage());
                                }
                            }

                        }
                    }
                });


    }


    public interface IResponseListener<T> {

        void onSuccess(T data);

        void onFail(int code,String message);
    }

    private static Gson buildGson(){
        if(gson==null){
            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .setLenient()
                    .registerTypeAdapter(Date.class,new DateTypeAdapter())
                    .registerTypeAdapter(new TypeToken<Map<String,Object>>(){
            }.getType(),new GsonTypeAdapter())
                    .create();
        }
        return gson;
    }
}
