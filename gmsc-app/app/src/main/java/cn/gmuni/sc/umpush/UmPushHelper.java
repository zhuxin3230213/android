package cn.gmuni.sc.umpush;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.entity.UMessage;
import com.umeng.message.tag.TagManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.app.SharedPreferencesHelper;
import cn.gmuni.sc.config.Const;
import cn.gmuni.sc.config.SharedPreferenceConst;
import cn.gmuni.sc.umpush.listeners.PushListenerManager;
import cn.gmuni.sc.umpush.push.NotificationModel;
import cn.gmuni.sc.umpush.push.UmNotificationClickListener;
import cn.gmuni.sc.utils.SystemUtils;

public class UmPushHelper {

    private static UmPushHelper instance = null;

    private PushAgent agent;

    private final Map<Integer,UmNotificationClickListener> listeners = new HashMap<>();

    private UmPushHelper(){
        init();
    }

    private void init(){
        //最后的值是在友盟申请的UMENG_MESSAGE_SECRE
        UMConfigure.init(BaseApplication.getInstance(),UMConfigure.DEVICE_TYPE_PHONE,"6c1a43e9abb86d5fa68f0230eced8432");
        agent = PushAgent.getInstance(BaseApplication.getInstance());
        agent.setResourcePackageName("cn.gmuni.sc");
        //友盟推送注册后事件
        agent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {
                //获取token成功
                Logger.i("token   "+s);
            }

            @Override
            public void onFailure(String s, String s1) {
                Logger.e("获取token失败:",s+"且s1="+s1);
            }
        });

        //自定义友盟推送点击事件
        customNotificationClickHandler();
        PushListenerManager.register(this);
    }

    /**
     * 自定义友盟推送点击事件
     */
    private void customNotificationClickHandler(){
        UmengNotificationClickHandler handler = new UmengNotificationClickHandler(){
            /**
             * 系统暂时使用的都是自定义消息
             * @param context
             * @param uMessage
             */
            @Override
            public void dealWithCustomAction(Context context, UMessage uMessage) {
                NotificationModel model = new NotificationModel();
                model.setText(uMessage.text);
                model.setTitle(uMessage.title);
                model.setTicker(uMessage.ticker);
                model.setExtraData(uMessage.extra);
                int flag = Integer.parseInt(uMessage.custom);
                UmNotificationClickListener listener = listeners.get(flag);
                //如果应用没有启动，则先调用父类启动应用
                if(!SystemUtils.isAppAlive(context)){
                    super.launchApp(context,uMessage);
                }
                if(listener!=null){
                    listener.onClick(context,model);
                }
            }

            @Override
            public void launchApp(Context context, UMessage uMessage) {
                super.launchApp(context, uMessage);
            }

            @Override
            public void openUrl(Context context, UMessage uMessage) {
                super.openUrl(context, uMessage);
            }

            @Override
            public void openActivity(Context context, UMessage uMessage) {
                super.openActivity(context, uMessage);
            }
        };

        agent.setNotificationClickHandler(handler);
    }

    /**
     * 给指定flag订阅推送回调函数
     * @param listener
     */
    public void subscribe(int flag,UmNotificationClickListener listener){
        listeners.put(flag,listener);
    }

    /**
     * 卸载指定flag订阅
     * @param flag
     */
    public void unSubscribe(int flag){
        listeners.remove(flag);
    }


    /**
     * 添加标签,可以给用户指定标签，按照标签进行推送
     * @param tags
     */
    public void setTags(String... tags){
        agent.getTagManager().addTags(new TagManager.TCallBack() {
            @Override
            public void onMessage(boolean b, ITagManager.Result result) {
                Logger.i("添加标签"+(b?"成功":"失败"));
                if(b){
                    //将标签记录到缓存中，方便退出登录时使用
                    SharedPreferencesHelper.Editor build = SharedPreferencesHelper.build();
                    String tg = (String) build.get(Const.SHARE_UMPUSH_TAGS,null);
                    if(tg==null || "".equals(tg)){
                        build.put(Const.SHARE_UMPUSH_TAGS,Arrays.toString(tags));
                    }else{
                        List<String> ts = Arrays.asList(tg.split(","));
                        for(String t : tags){
                            if(!ts.contains(t)){
                               ts.add(t);
                            }
                        }
                        build.put(Const.SHARE_UMPUSH_TAGS,Arrays.toString(ts.toArray()));
                    }
                }
            }
        },tags);
    }

    public void removeAllTags() {
        SharedPreferencesHelper.Editor build = SharedPreferencesHelper.build();
        String tg = (String) build.get(Const.SHARE_UMPUSH_TAGS, null);
        if(tg!=null && !"".equals(tg)){
            agent.getTagManager().deleteTags(new TagManager.TCallBack() {
                @Override
                public void onMessage(boolean b, ITagManager.Result result) {
                    build.remove(Const.SHARE_UMPUSH_TAGS);
                }
            },tg.split(","));
        }

    }


    public static UmPushHelper getInstance(){
        if(instance==null){
            instance = new UmPushHelper();
        }
        return instance;
    }
}
