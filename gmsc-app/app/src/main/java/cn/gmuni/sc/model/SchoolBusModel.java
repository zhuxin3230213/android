package cn.gmuni.sc.model;

import java.io.Serializable;

import cn.gmuni.sc.widget.recyclerview.model.Visitable;
import cn.gmuni.sc.widget.recyclerview.type.TypeFactory;

/**
 * @Author:ZhuXin
 * @Description:
 * @Date:Create 2018/12/26 13:27
 * @Modified By:
 **/
public class SchoolBusModel implements Serializable, Visitable {

    private String startCampus; //始发校区(编码)
    private String endCampus; //终到校区(编码)
    private int seasonState; //0：通用 1：夏季 2：冬季
    private String startCampusName;
    private String endCampusName;

    public SchoolBusModel() {
    }

    public String getStartCampus() {
        return startCampus;
    }

    public void setStartCampus(String startCampus) {
        this.startCampus = startCampus;
    }

    public String getEndCampus() {
        return endCampus;
    }

    public void setEndCampus(String endCampus) {
        this.endCampus = endCampus;
    }

    public int getSeasonState() {
        return seasonState;
    }

    public void setSeasonState(int seasonState) {
        this.seasonState = seasonState;
    }

    public String getStartCampusName() {
        return startCampusName;
    }

    public void setStartCampusName(String startCampusName) {
        this.startCampusName = startCampusName;
    }

    public String getEndCampusName() {
        return endCampusName;
    }

    public void setEndCampusName(String endCampusName) {
        this.endCampusName = endCampusName;
    }

    @Override
    public String toString() {
        return "SchoolBusModel{" +
                "startCampus='" + startCampus + '\'' +
                ", endCampus='" + endCampus + '\'' +
                ", seasonState=" + seasonState +
                ", startCampusName='" + startCampusName + '\'' +
                ", endCampusName='" + endCampusName + '\'' +
                '}';
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
