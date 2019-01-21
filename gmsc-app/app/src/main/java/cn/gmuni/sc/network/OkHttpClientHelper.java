package cn.gmuni.sc.network;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;

import cn.gmuni.sc.config.Const;
import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.utils.SecurityUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkHttpClientHelper {
    private static OkHttpClient okHttpClient = null;

    public static OkHttpClient getClient(){
        if(okHttpClient==null){
            synchronized (OkHttpClientHelper.class){
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                //设置连接超时时间，默认为10秒
                builder.connectTimeout(150, TimeUnit.SECONDS);
                //设置写入超时时间，默认为10秒
                builder.writeTimeout(150,TimeUnit.SECONDS);
                //设置读取数据超时时间
                builder.readTimeout(150,TimeUnit.SECONDS);
                //设置连接失败时是否重试
                builder.retryOnConnectionFailure(true);
                //设置头部信息拦截器
                builder.addInterceptor(getHeaderInterceptor());
                //处理自签名https请求
                builder.sslSocketFactory(HttpsTrustManager.createSSLSocketFactory(), new HttpsTrustManager())
                        .hostnameVerifier(new HttpsTrustManager.TrustAllHostnameVerifier());
                okHttpClient = builder.build();
            }
        }
       return  okHttpClient;
    }

    public static HashMap<String,String> listRequestHeaders(){
        HashMap<String,String> headers = new HashMap<>();
        List<Map<String,Object>> hd = SecurityUtils.listHeaders();
        for(Map<String,Object> header : hd){
            headers.put(header.get("name").toString(),header.get("value").toString());
        }
        UserInfoEntity userInfoEntity = SecurityUtils.getUserInfo();
        if (null!=userInfoEntity){
            if(null!=userInfoEntity.getSchool() && !"".equals(userInfoEntity.getSchool())){
                headers.put(Const.USER_COLLEGE_CODE,userInfoEntity.getSchool());
            }
        }
        return headers;
    }

    /**
     * 通过拦截器设置头信息
     * @return
     */
    private static Interceptor getHeaderInterceptor(){

        Interceptor in = chain -> {
            Request originalRequest = chain.request();
            Request.Builder requestBuilder = originalRequest.newBuilder()
                    .addHeader("Accept-Encoding", "gzip")
                    .addHeader("Accept", "application/json,image/*")
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Request-Device","app")//指定网络请求的设备
                    .method(originalRequest.method(), originalRequest.body());
            HashMap<String, String> headers = listRequestHeaders();
            for(Map.Entry<String,String> entry : headers.entrySet()){
                requestBuilder.addHeader(entry.getKey(),entry.getValue());
            }
            Request request = requestBuilder.build();
            return chain.proceed(request);
        };

        return in;
    }
}
