package cn.gmuni.sc.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.gmuni.sc.base.BaseActivity;
import cn.gmuni.sc.config.PermissionConst;
import cn.gmuni.sc.listeners.OnPermissionListener;
import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.IGprsApi;

import static android.content.Context.LOCATION_SERVICE;

/**
 * @Author:ZhuXin
 * @Description: 如果需要适配6.0以上系统请处理权限问题
 * @Date:Create 2018/11/28 10:40
 * @Modified By:
 **/
@SuppressLint("MissingPermission")
public class GPSUtils {

    private static LocationManager mLocationManager;

    private static final String TAG = "GPSUtils";

    private static Location mLocation = null;

    private static BaseActivity mContext;

    public GPSUtils(BaseActivity context) {

        this.mContext = context;
        mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        // 判断GPS是否正常启动
        //判断gprs是否开启
        if (!isOPen(context)) {
            getGPRSActivity(context); //获取GPRS权限
        }
        // 为获取地理位置信息时设置查询条件
        String bestProvider = mLocationManager.getBestProvider(getCriteria(), true);
        // 获取位置信息
        // 如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER
        if (bestProvider != null) {
            Location location = mLocationManager.getLastKnownLocation(bestProvider);
//        getLocationData(location);
            mLocation = location;
            //如果获取location为null时，更改获取方法
            if (location == null) {
                mLocation = getLastKnownLocation(context);
            }
            // 监听状态
//        mLocationManager.addGpsStatusListener(listener);

            // 绑定监听，有4个参数,设置自动监听位置
            // 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
            // 参数2，位置信息更新周期，单位毫秒
            // 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
            // 参数4，监听
            // 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新
            long minTime = 10000;
            float minDistance = 1;
            // 1秒更新一次，或最小位移变化超过1米更新一次；
            // 注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
            //  mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, locationListener);
        }
    }

    //GetLastKnownLocation输出结果永远是null处理办法
    private Location getLastKnownLocation(BaseActivity context) {
        mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    //获取GPRS权限
    public void getGPRSActivity(BaseActivity context) {
        context.requestPermission(PermissionConst.PERMISSION_ACCESS_FINE_LOCATION, new OnPermissionListener() {
            @Override
            public void onReceive() {
                //TODO 获取权限后操作
                if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(context, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
                    // 返回开启GPS导航设置界面
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivityForResult(intent, 0);
                    return;
                }
            }
        });
    }

    //通过WiFi或移动基站获取经纬度
    public void getGPRSDetailActivity(BaseActivity context) {
        context.requestPermission(PermissionConst.PERMISSION_ACCESS_COARSE_LOCATION, new OnPermissionListener() {
            @Override
            public void onReceive() {
                //TODO 获取权限后操作
            }
        });
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回查询条件
     *
     * @return
     */
    private static Criteria getCriteria() {
        Criteria criteria = new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        // 设置是否需要方位信息
        criteria.setBearingRequired(false);
        // 设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }


    /**
     * @return Location--->getLongitude()获取经度/getLatitude()获取纬度
     */
    public static Location getLocation() {
        if (mLocation == null) {
            Log.e("GPSUtils", "setLocationData: 获取当前位置信息为空");
            return null;
        }
        return mLocation;
    }

    public static String getLocalCity() {
        if (mLocation == null) {
            Log.e("GPSUtils", "getLocalCity: 获取城市信息为空");
            return "";
        }
        List<Address> result = getAddress(mLocation);

        String city = "";
        if (result != null && result.size() > 0) {
            city = result.get(0).getLocality();//获取城市
        }
        return city;
    }

    public static String getAddressStr() {
        if (mLocation == null) {
            Log.e("GPSUtils", "getAddressStr: 获取详细地址信息为空");
            return "";
        }
        List<Address> result = getAddress(mLocation);

        String address = "";
        if (result != null && result.size() > 0) {
            address = result.get(0).getAddressLine(0);//获取详细地址
        }
        return address;
    }

    // 位置监听
    private static LocationListener locationListener = new LocationListener() {

        //位置信息变化时触发
        public void onLocationChanged(Location location) {
            mLocation = location;
            Log.i(TAG, "时间：" + location.getTime());
            Log.i(TAG, "经度：" + location.getLongitude());
            Log.i(TAG, "纬度：" + location.getLatitude());
            Log.i(TAG, "海拔：" + location.getAltitude());
        }

        //GPS状态变化时触发
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                // GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    Log.i(TAG, "当前GPS状态为可见状态");
                    break;
                // GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    Log.i(TAG, "当前GPS状态为服务区外状态");
                    break;
                // GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.i(TAG, "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        //GPS开启时触发
        public void onProviderEnabled(String provider) {
            Location location = mLocationManager.getLastKnownLocation(provider);
            mLocation = location;
        }

        //GPS禁用时触发
        public void onProviderDisabled(String provider) {
            mLocation = null;
        }
    };

    // 获取地址信息
    private static List<Address> getAddress(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(mContext, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    // 状态监听
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                // 第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Log.i(TAG, "第一次定位");
                    break;
                // 卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    Log.i(TAG, "卫星状态改变");
                    GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
                    // 获取卫星颗数的默认最大值
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    // 创建一个迭代器保存所有卫星
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites()
                            .iterator();
                    int count = 0;
                    while (iters.hasNext() && count <= maxSatellites) {
                        GpsSatellite s = iters.next();
                        count++;
                    }
                    System.out.println("搜索到：" + count + "颗卫星");
                    break;
                // 定位启动
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.i(TAG, "定位启动");
                    break;
                // 定位结束
                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.i(TAG, "定位结束");
                    break;
            }
        }
    };

    //获取用户定位信息
    public Map<String, String> getGprsMessage() {
        Map<String, String> map = new HashMap<>();
        UserInfoEntity userInfo = SecurityUtils.getUserInfo();
        map.put("userInfo", String.valueOf(userInfo.getPhoneNumber()));
        map.put("school", String.valueOf(userInfo.getSchool()));
        if (!StringUtil.isEmpty(userInfo.getStudentId())) {
            //完善学籍信息后才能发送信息
            map.put("studentId", String.valueOf(userInfo.getStudentId()));
            map.put("gprsTime", String.valueOf(GPSUtils.getLocation().getTime()));
            map.put("longitude", String.valueOf(GPSUtils.getLocation().getLongitude())); //经度
            map.put("latitude", String.valueOf(GPSUtils.getLocation().getLatitude())); //纬度
            map.put("altitude", String.valueOf(GPSUtils.getLocation().getAltitude())); //海拔
            map.put("city", String.valueOf(GPSUtils.getLocalCity())); //城市
            map.put("address", String.valueOf(GPSUtils.getAddressStr())); //地址
        }
        return map;
    }

}
