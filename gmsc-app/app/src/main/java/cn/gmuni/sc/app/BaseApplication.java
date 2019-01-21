package cn.gmuni.sc.app;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;

import org.greenrobot.greendao.database.Database;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import cn.gmuni.sc.base.BaseActivity;
import cn.gmuni.sc.config.Const;
import cn.gmuni.sc.greendao.MySQLiteOpenHelper;
import cn.gmuni.sc.greendao.db.DaoMaster;
import cn.gmuni.sc.greendao.db.DaoSession;
import cn.gmuni.sc.umpush.UmPushHelper;
import cn.gmuni.sc.utils.SecurityUtils;

public class BaseApplication extends Application {

    private static final Properties props = new Properties();

    private static BaseApplication instance = null;

    private DaoSession daoSession;

    private Database db;

    //存储所有运行的activity
    private List<BaseActivity> cacheAllActivity = new ArrayList<>();
    private static Map<String, Activity> destoryMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();

        //将初始化对象赋值给instance
        instance = this;
        //初始化系统配置
        initSysConfig();
        //初始化友盟推送
        UmPushHelper.getInstance();
        //初始化greendao DaoSession 数据库
        setDatabase();
        //初始化数据缓存
        SharedPreferencesHelper.init(getBaseContext());
        //初始化时删除用户所有标签
        UmPushHelper.getInstance().removeAllTags();
        //初始化登录校验
        SecurityUtils.init();
        //初始化字体
        setFont();
        //初始化日志配置
        new LoggerAdapter().init();
        initGlobalTimeZone();


    }

    /**
     * 添加到销毁队列
     *
     * @param activity 要销毁的activity
     */
    public static void addDestoryActivity(Activity activity, String activityName) {
        destoryMap.put(activityName, activity);
    }

    /**
     * 销毁指定Activity
     */
    public static void destoryActivity(String activityName) {
        Set<String> keySet = destoryMap.keySet();
        if (keySet.size() > 0) {
            for (String key : keySet) {
                if (activityName.equals(key)) {
                    destoryMap.get(key).finish();
                }
            }
        }
    }

    //设置app内全局时区
    public void initGlobalTimeZone() {
        TimeZone chinaTimeZone = TimeZone.getTimeZone("GMT+8");
        TimeZone.setDefault(chinaTimeZone);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /**
     * 使用单例方便调用
     *
     * @return
     */
    public static BaseApplication getInstance() {
        return instance;
    }


    /**
     * 获取系统全局配置参数
     *
     * @param key 配置参数Key值
     * @return
     */
    public String getSysConfig(String key) {
        return props.getProperty(key);
    }

    /**
     * 初始化系统配置项
     */
    private void initSysConfig() {
        try {
            InputStream in = this.getAssets().open("config");
            props.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDatabase() {

        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this, Const.GREENDAO_DB_NAME, null);
        db = helper.getEncryptedWritableDb(Const.GREENDAO_DB_PWD);
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public Database getDb() {
        return db;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }


    /**
     * 当一个activity被打开时,记录当前activity
     *
     * @param activity
     */
    public void addActivity(BaseActivity activity) {
        cacheAllActivity.add(activity);
    }

    /**
     * 关闭所有运行的activity
     */
    public void finishActivities() {
        for (BaseActivity activity : cacheAllActivity) {
            activity.finish();
        }
        cacheAllActivity.clear();
    }

    /**
     * 关闭指定的activity
     *
     * @param activity
     */
    public void finishActivity(BaseActivity activity) {
        if (activity != null) {
            cacheAllActivity.remove(activity);
            activity.finish();
        }
    }

    /**
     * 获取当前显示的activity
     *
     * @return
     */
    public BaseActivity getCurrentActivity() {
        return cacheAllActivity.get(cacheAllActivity.size() - 1);
    }


    private void setFont() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/pingfang.ttf");
        try {
            Field field = Typeface.class.getDeclaredField("monospace");
            field.setAccessible(true);
            field.set(null, typeface);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
