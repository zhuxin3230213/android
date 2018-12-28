package cn.gmuni.sc.module.home.scan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.IllegalFormatCodePointException;
import java.util.logging.Logger;

import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.base.BaseWebViewActivity;
import cn.gmuni.sc.module.home.scan.base.BaseScan;
import cn.gmuni.sc.module.home.scan.constants.OrderCode;
import cn.gmuni.sc.utils.StringUtil;

import static cn.gmuni.sc.module.more.express.decoding.BarcodeFormat.DATA_MATRIX;
import static cn.gmuni.sc.module.more.express.decoding.BarcodeFormat.QR_CODE;

/**
 * @Author: zhuxin
 * @Date: 2018/11/13 17:51
 * @Description:
 */
public class DefaultScan extends BaseScan {

    int CODE_TYPE = -1; //标示 (1一维码、 2、二维码   3、其他码)
    @Override
    public boolean support(String scanResult, String barcodeFormat) {
        boolean flag = false;
        if (!StringUtil.isEmpty(barcodeFormat)) {
            if (!barcodeFormat.equals(QR_CODE.toString())){
                CODE_TYPE = 1;
                flag = true;
            }else {
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public int order() {
        return OrderCode.DEFAULT_CODE;
    }

    @Override
    public void execute(String scanResult) {
        Intent intent = new Intent(BaseApplication.getInstance().getBaseContext(), ScanDefaultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("scanResult",scanResult);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
