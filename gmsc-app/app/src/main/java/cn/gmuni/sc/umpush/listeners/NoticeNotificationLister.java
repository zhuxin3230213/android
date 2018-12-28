package cn.gmuni.sc.umpush.listeners;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.util.Map;
import cn.gmuni.sc.base.BaseWebViewActivity;
import cn.gmuni.sc.module.news.NewsActivity;
import cn.gmuni.sc.umpush.UmPushHelper;
import cn.gmuni.sc.umpush.push.NotificationModel;
import cn.gmuni.sc.umpush.push.UmNotificationClickListener;
import cn.gmuni.sc.utils.JsonUtils;

/**
 * @Author: zhuxin
 * @Date: 2018/9/29 17:53
 * @Description:
 */
public class NoticeNotificationLister extends UmNotificationClickListener {

    @Override
    public void onClick(Context context, NotificationModel model) {
        Map<String, Object> extraData = (Map<String, Object>) model.getExtraData();
        Object extra = extraData.get("extra");
        Map<String,Object> map = JsonUtils.parseJsonToMap((String) extra);
        String id = String.valueOf(map.get("id"));
        Intent intent = new Intent(context, BaseWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", "/notice/noticeDetail/" + id);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
