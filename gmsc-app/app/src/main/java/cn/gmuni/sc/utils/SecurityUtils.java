package cn.gmuni.sc.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.app.SharedPreferencesHelper;
import cn.gmuni.sc.base.BaseActivity;
import cn.gmuni.sc.config.Const;
import cn.gmuni.sc.config.PermissionConst;
import cn.gmuni.sc.config.SharedPreferenceConst;
import cn.gmuni.sc.database.UserInfoDao;
import cn.gmuni.sc.listeners.OnPermissionListener;
import cn.gmuni.sc.model.entities.UserInfoEntity;


/**
 * 系统鉴权工具类
 */
public class SecurityUtils {


    private static String token = null;

    private static List<Map<String,Object>> headers = null;

    private static UserInfoEntity entity = null;


    //初始化请求头
    private static void initHeaders(){
        headers = new ArrayList<>();
        token = null;
        Map<String,Object> header = null;
        SharedPreferencesHelper.Editor editor = SharedPreferencesHelper.build(SharedPreferenceConst.SHARE_LOGIN_NAMESPACE);
        //添加token
        if(editor.contains(Const.LOGIN_TOKEN_NAME)){
            token = (String)editor.get(Const.LOGIN_TOKEN_NAME, null);
            header= new HashMap<>();
            header.put("name",Const.LOGIN_TOKEN_NAME);
            header.put("value",token);
            headers.add(header);
        }
        if(editor.contains(Const.LOGIN_IDENTITY_TYPE)){
            header= new HashMap<>();
            header.put("name",Const.LOGIN_IDENTITY_TYPE);
            header.put("value",editor.get(Const.LOGIN_IDENTITY_TYPE,Const.IDENTITY_TYPE_PHONE));
            headers.add(header);
        }
    }

    public static void init(){
        initHeaders();
    }


    /**
     * 获取向后台访问的请求头
     * @return
     */
    public static List<Map<String,Object>> listHeaders(){
        if(headers == null){
            initHeaders();
        }
        return headers;
    }

    /**
     * 判断是否已登录
     * @return
     */
    public static boolean isLogin(){
        return token != null && !"".equals(token) && token.startsWith("Bearer");
    }

    /**
     * 是否设置的学校信息
     * @return
     */
    public static boolean isSettingCollege(){
        UserInfoEntity entity = getUserInfo();
       return entity!=null && entity.getSchool()!=null &&!"".equals(entity.getSchool());
    }

    /**
     * 获取用户信息
     * @return
     */
    public static UserInfoEntity getUserInfo(){
        if(entity==null){
            updateUserInfo();
        }
        return entity;
    }

    /**
     * 更新用户信息
     */
    public static void updateUserInfo(){
        UserInfoDao dao = new UserInfoDao();
        entity = dao.get();
        //清理缓存
        BaseApplication.getInstance().getDaoSession().clear();
    }


    /**
     * 退出登录调用方法
     */
    public static void logout(){
        headers = null;
        token = null;
        SharedPreferencesHelper.Editor editor = SharedPreferencesHelper.build(SharedPreferenceConst.SHARE_LOGIN_NAMESPACE);
        editor.clear();
    }


    /***
     * 获取当前用户设备唯一标识
     * @param context
     * @return
     */

    public static String getUniqueId(Context context){
        String serial = null;  // 35是IMEI开头的号
        String m_szDevIDShort = "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            // API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // serial需要一个初始化
             serial = "serial"; // 随便一个初始化
            exception.printStackTrace();
        }
        // 使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }








}
