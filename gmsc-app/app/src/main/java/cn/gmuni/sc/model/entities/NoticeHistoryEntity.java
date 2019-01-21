package cn.gmuni.sc.model.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Author: zhuxin
 * @Date: 2018/10/19 10:46
 * @Description:
 */
@Entity
public class NoticeHistoryEntity {

    @Id
    private String id;

    private String noticeId;
    private String userInfo; //当前用户
    private String campus; //当前学校
    private boolean isCheck; //标记当前用户是否已读


    @Generated(hash = 1504069835)
    public NoticeHistoryEntity(String id, String noticeId, String userInfo,
            String campus, boolean isCheck) {
        this.id = id;
        this.noticeId = noticeId;
        this.userInfo = userInfo;
        this.campus = campus;
        this.isCheck = isCheck;
    }
    @Generated(hash = 1436814887)
    public NoticeHistoryEntity() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNoticeId() {
        return this.noticeId;
    }
    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
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
    public boolean getIsCheck() {
        return this.isCheck;
    }
    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

}
