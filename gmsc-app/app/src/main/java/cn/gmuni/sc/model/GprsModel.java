package cn.gmuni.sc.model;

import java.io.Serializable;

import cn.gmuni.sc.widget.recyclerview.model.Visitable;
import cn.gmuni.sc.widget.recyclerview.type.TypeFactory;

/**
 * @Author:ZhuXin
 * @Description: 学生位置信息
 * @Date:Create 2018/11/30 10:14
 * @Modified By:
 **/
public class GprsModel implements Serializable, Visitable {

    private String gprsTime;
    private String gprsAddress;

    public GprsModel() {
    }

    public String getGprsTime() {
        return gprsTime;
    }

    public void setGprsTime(String gprsTime) {
        this.gprsTime = gprsTime;
    }

    public String getGprsAddress() {
        return gprsAddress;
    }

    public void setGprsAddress(String gprsAddress) {
        this.gprsAddress = gprsAddress;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
