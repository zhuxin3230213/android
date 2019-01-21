package cn.gmuni.sc.module.home.scan.qr_manager;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import cn.gmuni.sc.module.home.scan.ComparatorImpl;
import cn.gmuni.sc.module.home.scan.DefaultScan;
import cn.gmuni.sc.module.home.scan.HairDryerScan;
import cn.gmuni.sc.module.home.scan.WebScan;
import cn.gmuni.sc.module.home.scan.model.Scan;
import cn.gmuni.sc.utils.MToast;

/**
 * @Author: zhuxin
 * @Date: 2018/11/6 11:15
 * @Description: 注册类结果集
 */
public class QrcodeManager {

    private static List<Scan> managers = new ArrayList<>();

    static {
        managers.add(new WebScan());
        managers.add(new DefaultScan());
        managers.add(new HairDryerScan());
        //对象升序排列
        Comparator comp = new ComparatorImpl();
        Collections.sort(managers, comp);

    }

    //执行结果
    public static void execute(String scanResult, String barcodeFormat) {
        //遍历结果看该实现类是否符合规则
        for (Scan scan : managers) {
            if (scan.support(scanResult, barcodeFormat)) {
                scan.execute(scanResult);
                break;
            }
        }
    }

}
