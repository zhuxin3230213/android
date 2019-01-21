package cn.gmuni.sc.module.home.scan.base;

import android.content.Intent;
import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.module.home.scan.model.Scan;

/**
 * @Author: zhuxin
 * @Date: 2018/11/7 09:39
 * @Description: 启动activity
 */
public abstract class BaseScan implements Scan {

    public void startActivity(Intent intent) {
        BaseApplication.getInstance().startActivity(intent);
    }
}
