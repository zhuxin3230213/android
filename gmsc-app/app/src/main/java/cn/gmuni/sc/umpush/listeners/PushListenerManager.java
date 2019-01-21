package cn.gmuni.sc.umpush.listeners;



import cn.gmuni.sc.umpush.UmPushHelper;
import cn.gmuni.sc.umpush.push.UmPushFlag;

/**
 * 注册推送消息监听
 */
public class PushListenerManager {

    private static NewsNotificationLister newsLister = new NewsNotificationLister();
    private static NoticeNotificationLister noticeLister = new NoticeNotificationLister();
    private static MessageNotificationLister messageLister = new MessageNotificationLister();

    public static void register(UmPushHelper helper){
        helper.subscribe(UmPushFlag.NEWS_FLAG,newsLister);
        helper.subscribe(UmPushFlag.NOTICE_FLAG,noticeLister);
        helper.subscribe(UmPushFlag.MESSAGE_FLAG,messageLister);
    }
}
