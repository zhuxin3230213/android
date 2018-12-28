package cn.gmuni.sc.umpush.listeners;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Map;

import cn.gmuni.sc.model.MessageModel;
import cn.gmuni.sc.module.me.message.MessageNoActivity;
import cn.gmuni.sc.umpush.push.NotificationModel;
import cn.gmuni.sc.umpush.push.UmNotificationClickListener;
import cn.gmuni.sc.utils.JsonUtils;

/**
 * @Author:ZhuXin
 * @Description:
 * @Date:Create 2018/11/22 10:51
 * @Modified By:
 **/
public class MessageNotificationLister extends UmNotificationClickListener {

    @Override
    public void onClick(Context context, NotificationModel model) {
        //获取推送消息
        Map<String, Object> extraData = (Map<String, Object>) model.getExtraData();
        Object extra = extraData.get("extra");
        Map<String, Object> map = JsonUtils.parseJsonToMap((String) extra);

        MessageModel messageModel = new MessageModel();
        messageModel.setContent(String.valueOf(map.get("content")));
        messageModel.setDeptName(String.valueOf(map.get("deptName")));
        messageModel.setPublisher(String.valueOf(map.get("publisher")));

        //跳转正文详情页
        Intent intent = new Intent(context, MessageNoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("message", messageModel);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
