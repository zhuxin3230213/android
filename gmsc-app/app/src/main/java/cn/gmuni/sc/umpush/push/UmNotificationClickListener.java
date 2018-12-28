package cn.gmuni.sc.umpush.push;


import android.content.Context;
import android.content.Intent;

import cn.gmuni.sc.app.BaseApplication;

/**
 * 友盟推送点击事件
 */
public abstract class UmNotificationClickListener {

     public abstract void onClick(Context context, NotificationModel model);

     protected void startActivity(Intent intent){
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          BaseApplication.getInstance().startActivity(intent);
     }
}
