package cn.gmuni.sc.umpush.listeners;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import cn.gmuni.sc.base.BaseWebViewActivity;
import cn.gmuni.sc.module.news.NewsActivity;
import cn.gmuni.sc.umpush.push.NotificationModel;
import cn.gmuni.sc.umpush.push.UmNotificationClickListener;
import cn.gmuni.sc.utils.JsonUtils;

/**
 * @Author: zhuxin
 * @Date: 2018/9/29 16:11
 * @Description:
 */
public class NewsNotificationLister extends UmNotificationClickListener {

    @Override
    public void onClick(Context context, NotificationModel model) {
        Map<String, Object> extraData = (Map<String, Object>) model.getExtraData();
        Object extra = extraData.get("extra");
        Map<String, Object> map = JsonUtils.parseJsonToMap((String) extra);
        String id = String.valueOf(map.get("id"));
        Intent intent = new Intent(context, BaseWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", "/news/newsDetail/" + id);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
