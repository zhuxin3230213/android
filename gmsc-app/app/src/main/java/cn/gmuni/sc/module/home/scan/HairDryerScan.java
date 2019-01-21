package cn.gmuni.sc.module.home.scan;

import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.base.BaseWebViewActivity;
import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.module.home.scan.base.BaseScan;
import cn.gmuni.sc.module.home.scan.constants.OrderCode;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.IHairDryerApi;
import cn.gmuni.sc.utils.DateUtils;
import cn.gmuni.sc.utils.DesUtils;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.utils.StringUtil;

import static cn.gmuni.sc.module.more.express.decoding.BarcodeFormat.QR_CODE;

/**
 * @Author:ZhuXin
 * @Description: 吹风机扫码
 * @Date:Create 2018/12/6 10:22
 * @Modified By:
 **/
public class HairDryerScan extends BaseScan {

    int CODE_TYPE = -1;      //标示 (1一维码、 2、二维码   3、其他码)

    @Override
    public boolean support(String scanResult, String barcodeFormat) {
        boolean flag = false;
        //扫描获取的编码格式不为空
        if (!StringUtil.isEmpty(barcodeFormat)) {
            //拍码后返回的编码格式
            if (barcodeFormat.equals(QR_CODE.toString())) {
                CODE_TYPE = 2;
                //吹风机扫码规则
                //8PKPinkdmzs= 生成二维码
                //解析后:h 1001
                String gmuni = DesUtils.decode(scanResult, "gmuni");
                if (gmuni.startsWith("h")) {
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
        return OrderCode.HAIR_DRYER_CODE;
    }

    @Override
    public void execute(String scanResult) {
        //吹风机设备id
        String deviceId = DesUtils.decode(scanResult, "gmuni").substring(2);
        checkHairDryerStatus(deviceId);
    }

    //获取吹风机状态
    private void checkHairDryerStatus(String deviceId) {
        UserInfoEntity userInfo = SecurityUtils.getUserInfo();
        Map<String, String> map = new HashMap<>();
        map.put("blowerCode", deviceId);
        map.put("schoolCode", userInfo.getSchool());
        getData(map, deviceId); //获取数据
    }

    /**
     * 获取数据
     * 用户可以扫码多台设备
     */
    private void getData(Map<String, String> map, String deviceId) {
        BaseApplication.getInstance().getCurrentActivity().showLoading();
        Network.request(Network.createApi(IHairDryerApi.class).getHairDryerStatus(map), new Network.IResponseListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                if (data != null) {
                    if ("true".equals(String.valueOf(data.get("flag")))) {
                        toPayWeb(deviceId);
                    } else {
                        String status = String.valueOf(data.get("status"));
                        //status : 0   message：机器故障，无法使用
                        //status : 1   message：机器正在使用，请稍后
                        //status : 3   未支付状态（锁定状态）
                        //status : 2   正在使用    time：开始工作时间，workTime：工作总时长（分钟）
                        if ("0".equals(status)) {
                            MToast.showLongToast("设备已报修,等待小哥哥维护,请使用附近其他设备。");
                        } else if ("1".equals(status)) {
                            MToast.showLongToast("吹风机正在被使用,请稍等。"); //中间层已经校验是否是该用户扫码
                        } else if ("3".equals(status)) {
                            //该用户未支付情况(为用户使用做排序)
                            //MToast.showLongToast("有用户在使用，未支付，已进入锁定状态");
                            toPayWeb(deviceId);
                        } else if ("2".equals(status)) {
                            //该用户已经支付正在使用吹风机
                            //MToast.showLongToast("有用户在使用,稍后再试，开始工作时间:" + DateUtils.long2Data(String.valueOf(data.get("time")), DateUtils.COMMON_DATETIME) + "工作时长" + String.valueOf(data.get("workTime")) + "分钟");
                            toUserUseWeb(deviceId, status, DateUtils.long2Data(String.valueOf(data.get("time")), DateUtils.COMMON_DATETIME), String.valueOf(data.get("workTime")));
                        }
                    }
                } else {
                    MToast.showLongToast("该设备不存在,请核对后使用！");
                }
                BaseApplication.getInstance().getCurrentActivity().hideLoading();
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取吹风机状态失败");
                BaseApplication.getInstance().getCurrentActivity().hideLoading();
            }
        });
    }


    //跳转支付网页
    private void toPayWeb(String deviceId) {
        Intent intent = new Intent(BaseApplication.getInstance().getBaseContext(), BaseWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", "/hairDryer/to/" + deviceId);
        intent.putExtras(bundle);
        BaseApplication.addDestoryActivity(BaseApplication.getInstance().getCurrentActivity(),"CaptureScanActivity");//添加到销毁队列
        BaseApplication.destoryActivity("CaptureScanActivity"); //销毁指定的activity
        startActivity(intent);
    }


    //该用户已经支付，正在使用
    private void toUserUseWeb(String deviceId, String status, String startTime, String workTime) {
        Intent intent = new Intent(BaseApplication.getInstance().getBaseContext(), BaseWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", "/hairDryer/to/use/" + deviceId + "/" + status + "/" + startTime + "/" + workTime);
        intent.putExtras(bundle);
        BaseApplication.addDestoryActivity(BaseApplication.getInstance().getCurrentActivity(),"CaptureScanActivity");
        BaseApplication.destoryActivity("CaptureScanActivity"); //销毁指定的activity
        startActivity(intent);
    }

}
