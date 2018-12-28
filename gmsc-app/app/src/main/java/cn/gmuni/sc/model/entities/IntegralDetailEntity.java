package cn.gmuni.sc.model.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Author: zhuxin
 * @Date: 2018/11/2 10:01
 * @Description: 签到状态保存
 */
@Entity
public class IntegralDetailEntity {

    @Id
    private String id; //id

    private String inYear;
    private String inMonth;
    private String inDay;
    private String state; //积分获取状态
    private String integral; //获取的积分
    private String userInfo; //所属登录用户
    private String campus; //所属学校
    private String taskCode; //任务编码

    @Generated(hash = 295958460)
    public IntegralDetailEntity(String id, String inYear, String inMonth,
                                String inDay, String state, String integral, String userInfo,
                                String campus, String taskCode) {
        this.id = id;
        this.inYear = inYear;
        this.inMonth = inMonth;
        this.inDay = inDay;
        this.state = state;
        this.integral = integral;
        this.userInfo = userInfo;
        this.campus = campus;
        this.taskCode = taskCode;
    }

    @Generated(hash = 724699514)
    public IntegralDetailEntity() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInYear() {
        return this.inYear;
    }

    public void setInYear(String inYear) {
        this.inYear = inYear;
    }

    public String getInMonth() {
        return this.inMonth;
    }

    public void setInMonth(String inMonth) {
        this.inMonth = inMonth;
    }

    public String getInDay() {
        return this.inDay;
    }

    public void setInDay(String inDay) {
        this.inDay = inDay;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIntegral() {
        return this.integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getUserInfo() {
        return this.userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getCampus() {
        return this.campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getTaskCode() {
        return this.taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }


}
