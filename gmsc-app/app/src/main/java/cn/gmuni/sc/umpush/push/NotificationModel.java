package cn.gmuni.sc.umpush.push;

import java.io.Serializable;

/**
 * 友盟推送返回的数据类型
 */
public class NotificationModel implements Serializable{
    //通知栏提示文字
    private String ticker;

    //通知栏标题
    private String title;

    //通知栏消息信息
    private String text;

    /**
     * 推送消息对应的标记 ,如校内新闻推送，通知公告推送，更新推送等等
     */
    private int flag;

    /**
     * 推送额外的数据
     */
    private Object extraData;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Object getExtraData() {
        return extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }
}
