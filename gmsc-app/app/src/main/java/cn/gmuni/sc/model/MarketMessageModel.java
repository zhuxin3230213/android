package cn.gmuni.sc.model;

import java.io.Serializable;

import cn.gmuni.sc.widget.recyclerview.model.Visitable;
import cn.gmuni.sc.widget.recyclerview.type.TypeFactory;

/**
 * @Author:ZhuXin
 * @Description: 集市留言
 * @Date:Create 2019/1/16 10:27
 * @Modified By:
 **/
public class MarketMessageModel implements Serializable, Visitable {

    /**
     * 唯一标识
     */
    private String id;
    /**
     * 所留言的集市商品id
     */
    private String marketId;
    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 时间
     */
    private String createTime;
    /**
     * 留言者
     */
    private String messageUser;

    /**
     * 学校
     */
    private String schoolName;

    /**
     * 留言信息
     */
    private String message;

    /**
     * 回复者
     */
    private String replier;

    /**
     * 信息发布者与留言者相同标记
     * 0：相同 1：不同
     */
    private String remarkPublishIsMessageUser;
    /**
     * 昵称
     */
    private String userName;

    private Object children;

    public MarketMessageModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReplier() {
        return replier;
    }

    public void setReplier(String replier) {
        this.replier = replier;
    }

    public String getRemarkPublishIsMessageUser() {
        return remarkPublishIsMessageUser;
    }

    public void setRemarkPublishIsMessageUser(String remarkPublishIsMessageUser) {
        this.remarkPublishIsMessageUser = remarkPublishIsMessageUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Object getChildren() {
        return children;
    }

    public void setChildren(Object children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "MarketMessageModel{" +
                "id='" + id + '\'' +
                ", marketId='" + marketId + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickName='" + nickName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", messageUser='" + messageUser + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", message='" + message + '\'' +
                ", replier='" + replier + '\'' +
                ", remarkPublishIsMessageUser='" + remarkPublishIsMessageUser + '\'' +
                ", userName='" + userName + '\'' +
                ", children=" + children +
                '}';
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
