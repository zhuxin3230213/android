package cn.gmuni.sc.config;

import android.Manifest;

import java.util.HashMap;
import java.util.Map;

public class PermissionConst {

    //允许程序录制声音通过手机或耳机的麦克
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    //允许程序访问电话状态
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    //允许程序从非系统拨号器里拨打电话
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    //允许程序访问摄像头进行拍照
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    //允许程序通过GPS芯片接收卫星的定位信息
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    //允许程序通过WiFi或移动基站的方式获取用户错略的经纬度信息
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    //程序可以读取设备外部存储空间（内置SDcard和外置SDCard）的文件，如果您的App已经添加了“WRITE_EXTERNAL_STORAGE ”权限 ，则就没必要添加读的权限了，写权限已经包含了读权限了。
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    //允许程序写入外部存储,如SD卡上写文件
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private static Map<String,String> rejectMessages = new HashMap<>();

    static {
        rejectMessages.put(PERMISSION_RECORD_AUDIO,"获取录音权限失败");
        rejectMessages.put(PERMISSION_READ_PHONE_STATE,"获取电话状态权限失败");
        rejectMessages.put(PERMISSION_CALL_PHONE,"获取拨打电话权限失败");
        rejectMessages.put(PERMISSION_CAMERA,"获取相机权限失败");
        rejectMessages.put(PERMISSION_ACCESS_FINE_LOCATION,"获取GPS权限失败");
        rejectMessages.put(PERMISSION_ACCESS_COARSE_LOCATION,"获取经纬度坐标权限失败");
        rejectMessages.put(PERMISSION_READ_EXTERNAL_STORAGE,"获取读取设备外部存储空间权限失败");
        rejectMessages.put(PERMISSION_WRITE_EXTERNAL_STORAGE,"获取写入设备外部存储空间权限失败");
    }

    public static String getErrorMsg(String permission){
        return rejectMessages.get(permission);
    }
}
