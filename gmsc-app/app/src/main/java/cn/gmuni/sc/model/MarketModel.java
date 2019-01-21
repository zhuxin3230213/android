package cn.gmuni.sc.model;

import java.io.Serializable;
import java.util.ArrayList;

import cn.gmuni.sc.widget.recyclerview.model.Visitable;
import cn.gmuni.sc.widget.recyclerview.type.TypeFactory;

/**
 * @Author:ZhuXin
 * @Description:
 * @Date:Create 2019/1/9 11:55
 * @Modified By:
 **/
public class MarketModel implements Serializable, Visitable {

    /**
     * 唯一标识
     */
    private String id;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 发布者的唯一标识
     */
    private String indentifier;
    /**
     * 发布的用户
     */
    private String userName;
    /**
     * 登录用户
     */
    private String userInfo;

    /**
     * 学校
     */
    private String schoolName;

    /**
     * 出售价格
     */
    private String price;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述缩略图片
     */
    private String imgS;

    /**
     * 描述大图
     */
    private String imgB;

    /**
     * 消息统计
     */
    private String meaageCount;

    /**
     * 介绍
     */
    private String introduce;

    /**
     * 出售物品类型
     */
    private String type;

    /**
     * 出售状态(0:未出售 1:已出售)
     */
    private String saleStatus;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 举报锁定状态：(0:正常，1:锁定)
     */
    private String status;
    private ArrayList list;

    public MarketModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getIndentifier() {
        return indentifier;
    }

    public void setIndentifier(String indentifier) {
        this.indentifier = indentifier;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgS() {
        return imgS;
    }

    public void setImgS(String imgS) {
        this.imgS = imgS;
    }

    public String getImgB() {
        return imgB;
    }

    public void setImgB(String imgB) {
        this.imgB = imgB;
    }

    public String getMeaageCount() {
        return meaageCount;
    }

    public void setMeaageCount(String meaageCount) {
        this.meaageCount = meaageCount;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList getList() {
        return list;
    }

    public void setList(ArrayList list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "MarketModel{" +
                "id='" + id + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickName='" + nickName + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", indentifier='" + indentifier + '\'' +
                ", userName='" + userName + '\'' +
                ", userInfo='" + userInfo + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", price='" + price + '\'' +
                ", title='" + title + '\'' +
                ", imgS='" + imgS + '\'' +
                ", imgB='" + imgB + '\'' +
                ", meaageCount='" + meaageCount + '\'' +
                ", introduce='" + introduce + '\'' +
                ", type='" + type + '\'' +
                ", saleStatus='" + saleStatus + '\'' +
                ", createTime='" + createTime + '\'' +
                ", status='" + status + '\'' +
                ", list=" + list +
                '}';
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
