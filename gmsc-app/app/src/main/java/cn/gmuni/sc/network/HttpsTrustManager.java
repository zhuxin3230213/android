package cn.gmuni.sc.network;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import cn.gmuni.sc.app.BaseApplication;

public class HttpsTrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {

        return new X509Certificate[0];
    }


    public static SSLSocketFactory createSSLSocketFactory() {
        try {
            String sslMode = BaseApplication.getInstance().getSysConfig("ssl.mode");
            Logger.i(sslMode);
            String cerFileName = "test.cer";
            String aliasName = "root";
            //如果是生产环境，则修改为正式证书
            if("prod".equals(sslMode)){
                cerFileName = "gmuni.cer";
                aliasName = "intermediate1";
            }
            InputStream cer = BaseApplication.getInstance().getAssets().open(cerFileName);
            InputStream chsicer = BaseApplication.getInstance().getAssets().open("www.chsi.com.cn.cer");
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null,null);
            keyStore.setCertificateEntry(aliasName,factory.generateCertificate(cer));
            //学信网证书
            keyStore.setCertificateEntry("chsi.com.cn",factory.generateCertificate(chsicer));
            if(cer!=null){
                cer.close();
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null,trustManagerFactory.getTrustManagers(),new SecureRandom());

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public   static  class  TrustAllHostnameVerifier implements  HostnameVerifier
    {
        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return   true;
        }
    }


}
