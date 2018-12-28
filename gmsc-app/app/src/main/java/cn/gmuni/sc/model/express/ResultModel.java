package cn.gmuni.sc.model.express;

import java.io.Serializable;

import cn.gmuni.sc.widget.recyclerview.model.Visitable;
import cn.gmuni.sc.widget.recyclerview.type.TypeFactory;

/**
 * @Author: zhuxin
 * @Date: 2018/9/20 17:45
 * @Description:
 */
public class ResultModel implements Serializable,Visitable {
       private  String name;
       private  String expNo;
       private  String logo;
       private  String currentTime;
       private  String currentDate;
       private  String detail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpNo() {
        return expNo;
    }

    public void setExpNo(String expNo) {
        this.expNo = expNo;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
