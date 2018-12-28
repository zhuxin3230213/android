package cn.gmuni.sc.module.home.scan;

import android.content.Intent;
import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.base.BaseWebViewActivity;
import cn.gmuni.sc.module.home.scan.base.BaseScan;
import cn.gmuni.sc.module.home.scan.constants.OrderCode;
import cn.gmuni.sc.utils.StringUtil;

import static cn.gmuni.sc.module.more.express.decoding.BarcodeFormat.QR_CODE;

/**
 * @Author: zhuxin
 * @Date: 2018/11/7 09:36
 * @Description: 实现类
 */
public class WebScan extends BaseScan {

    int CODE_TYPE = -1;      //标示 (1一维码、 2、二维码   3、其他码)

    //扫码规则
    @Override
    public boolean support(String scanResult, String barcodeFormat) {
        boolean flag = false;
        //扫描获取的编码格式不为空
        if (!StringUtil.isEmpty(barcodeFormat)) {
            //拍码后返回的编码格式
            if (barcodeFormat.equals(QR_CODE.toString())) {
                CODE_TYPE = 2;
                //扫描二维码结果:https\http,其他
                if (scanResult.startsWith("http:") || scanResult.startsWith("https:")) {
                    flag = true;
                } else {
                    flag = false;
                }
            } else {
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public int order() {
        return OrderCode.WEB_CODE;
    }

    //符合规则后执行的操作
    @Override
    public void execute(String scanResult) {
        Intent intent = new Intent(BaseApplication.getInstance().getBaseContext(), BaseWebViewActivity.class);
        intent.putExtra("url", scanResult);
        startActivity(intent);
    }
}
