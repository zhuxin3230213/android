package cn.gmuni.sc.model.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Author: zhuxin
 * @Date: 2018/9/25 09:24
 * @Description:
 */
@Entity
public class ExpressHistoryEntity {

    @Id
    private String id; //id

    private String companyName; //公司名称
    private String expNo; //运单号码
    private String companyLogo; //logo
    private String currentDate;  //物流最新日期
    private String detail; //物流详情
    private String code; //公司编码
    private String userInfo; //当前用户
    private String campus; //当前学校
    private String state; //当前状态

    @Generated(hash = 425870688)
    public ExpressHistoryEntity(String id, String companyName, String expNo,
            String companyLogo, String currentDate, String detail, String code,
            String userInfo, String campus, String state) {
        this.id = id;
        this.companyName = companyName;
        this.expNo = expNo;
        this.companyLogo = companyLogo;
        this.currentDate = currentDate;
        this.detail = detail;
        this.code = code;
        this.userInfo = userInfo;
        this.campus = campus;
        this.state = state;
    }
    @Generated(hash = 353625575)
    public ExpressHistoryEntity() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCompanyName() {
        return this.companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getExpNo() {
        return this.expNo;
    }
    public void setExpNo(String expNo) {
        this.expNo = expNo;
    }
    public String getCompanyLogo() {
        return this.companyLogo;
    }
    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }
    public String getCurrentDate() {
        return this.currentDate;
    }
    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
    public String getDetail() {
        return this.detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
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
    public String getState() {
        return this.state;
    }
    public void setState(String state) {
        this.state = state;
    }

  


}
