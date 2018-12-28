package cn.gmuni.sc.model;

import java.io.Serializable;

import cn.gmuni.sc.widget.recyclerview.model.Visitable;
import cn.gmuni.sc.widget.recyclerview.type.TypeFactory;

public class MessageModel implements Visitable, Serializable {


    private String id; //唯一标示
    private String title; //标题
    private String content; //正文
    private String publisher; //发布人
    private String deptName; //发布人所属机构
    private String userInfo; //登录用户姓名
    private String campus; //所属学校
    private String typeName; //信息类型： 网费、通知
    private String time; //发送时间
    private String state; //阅读状态: 0: 未读 1:已读
    private String studentCode; //学号
    private String tradeNo; //缴费订单号
    private String totalAmount; //出账金额
    private String netId; //上网账号
    private String netType; //费用类型： 0:支出 1:收入

    public MessageModel() {
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", publisher='" + publisher + '\'' +
                ", deptName='" + deptName + '\'' +
                ", userInfo='" + userInfo + '\'' +
                ", campus='" + campus + '\'' +
                ", typeName='" + typeName + '\'' +
                ", time='" + time + '\'' +
                ", state='" + state + '\'' +
                ", studentCode='" + studentCode + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", netId='" + netId + '\'' +
                ", netType='" + netType + '\'' +
                '}';
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
