package cn.gmuni.sc.model.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

@Entity
public class UserInfoEntity {

    @Id
    //id
    private String id;


    //手机号码
    private String phoneNumber;

    //微信号
    private String wechat;

    //qq
    private String qq;

    //邮箱
    private String email;

    //微博
    private String weibo;

    //所属院校编码
    private String school;

    //学号
    private String studentId;

    //所属院系
    private String faculty;

    //所属专业
    private String subject;

    //所属班级
    private String clazz;

    //头像
    private String avatar;

    //名称
    private String name;

    //性别
    private String sex;

    //生日
    private Date birthday;

    //身份证号码
    private String identityCard;

    //真实姓名
    private String realName;

    //所属院校名称，不保存数据库
    private String schoolName;

    //上网账号
    private String netAccount;

    @Generated(hash = 555154978)
    public UserInfoEntity(String id, String phoneNumber, String wechat, String qq,
            String email, String weibo, String school, String studentId,
            String faculty, String subject, String clazz, String avatar,
            String name, String sex, Date birthday, String identityCard,
            String realName, String schoolName, String netAccount) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.wechat = wechat;
        this.qq = qq;
        this.email = email;
        this.weibo = weibo;
        this.school = school;
        this.studentId = studentId;
        this.faculty = faculty;
        this.subject = subject;
        this.clazz = clazz;
        this.avatar = avatar;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.identityCard = identityCard;
        this.realName = realName;
        this.schoolName = schoolName;
        this.netAccount = netAccount;
    }

    @Generated(hash = 2042969639)
    public UserInfoEntity() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWechat() {
        return this.wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getQq() {
        return this.qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeibo() {
        return this.weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getSchool() {
        return this.school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFaculty() {
        return this.faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getClazz() {
        return this.clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIdentityCard() {
        return this.identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSchoolName() {
        return this.schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getNetAccount() {
        return netAccount;
    }

    public void setNetAccount(String netAccount) {
        this.netAccount = netAccount;
    }
}
