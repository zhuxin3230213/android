package cn.gmuni.sc.model;

import java.io.Serializable;

import cn.gmuni.sc.widget.recyclerview.model.Visitable;
import cn.gmuni.sc.widget.recyclerview.type.TypeFactory;

/**
 * @Author: zhuxin
 * @Date: 2018/8/31 15:22
 * @Description: 通知
 */
public class NoticeModel implements Visitable,Serializable {

    private String id;
    private String title;
    private String name;
    private String content;
    private String updateTime;
    private Integer clickThrough;
    private String attachment;
    private boolean isCheck;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getClickThrough() {
        return clickThrough;
    }

    public void setClickThrough(Integer clickThrough) {
        this.clickThrough = clickThrough;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public String toString() {
        return "NoticeModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", clickThrough=" + clickThrough +
                ", attachment='" + attachment + '\'' +
                ", isCheck=" + isCheck +
                '}';
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
