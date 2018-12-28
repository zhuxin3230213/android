package cn.gmuni.sc.model;

import java.io.Serializable;
import java.util.Date;

import cn.gmuni.sc.widget.recyclerview.model.Visitable;
import cn.gmuni.sc.widget.recyclerview.type.TypeFactory;

public class LostFoundModel implements Serializable, Visitable{

    /**
     * id
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 物品名称
     */
    private String objName;

    /**
     * 地点
     */
    private String objAddress;

    /**
     * 时间
     */
    private Date objTime;
    /**
     * 详细描述
     */
    private String description;
    /**
     * 提交用户
     */
    private String userInfo;

    /**
     * 所属学校
     */
    private String campus;

    /**
     * 信息提交时间
     */
    private Date createTime;

    /**
     * 类型{0：丢失，1：捡到}
     */
    private String lfType;

    /**
     * 状态{0：激活，1：完结}
     */
    private String infoStatus;

    /**
     * 缩略图
     */
    private String snapshot;

    /**
     * 大图
     */
    private String image;

    /**
     * 头像
     */
    private String userImg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getObjAddress() {
        return objAddress;
    }

    public void setObjAddress(String objAddress) {
        this.objAddress = objAddress;
    }

    public Date getObjTime() {
        return objTime;
    }

    public void setObjTime(Date objTime) {
        this.objTime = objTime;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLfType() {
        return lfType;
    }

    public void setLfType(String lfType) {
        this.lfType = lfType;
    }

    public String getInfoStatus() {
        return infoStatus;
    }

    public void setInfoStatus(String infoStatus) {
        this.infoStatus = infoStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    @Override
    public String toString() {
        return "LostFoundModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", objName='" + objName + '\'' +
                ", objAddress='" + objAddress + '\'' +
                ", objTime='" + objTime + '\'' +
                ", description='" + description + '\'' +
                ", campus=" + campus + '\'' +
                ", createTime=" + createTime + '\'' +
                ", lfType=" + lfType + '\'' +
                ", infoStatus=" + infoStatus + '\'' +
                ", snapshot=" + snapshot + '\'' +
                ", image=" + image +
                ", userImg=" + userImg +
                '}';
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
