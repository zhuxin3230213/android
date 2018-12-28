package cn.gmuni.sc.model;

import java.io.Serializable;

import cn.gmuni.sc.widget.recyclerview.model.Visitable;
import cn.gmuni.sc.widget.recyclerview.type.TypeFactory;

/**
 * @Author: zhuxin
 * @Date: 2018/10/26 10:18
 * @Description:
 * 新闻、通知尾部信息提示
 */
public class InforTipsModel implements Serializable, Visitable {


    private String informationTips; //尾部信息提示

    public String getInformationTips() {
        return informationTips;
    }

    public void setInformationTips(String informationTips) {
        this.informationTips = informationTips;
    }

    @Override
    public String toString() {
        return "InforTipsModel{" +
                "informationTips='" + informationTips + '\'' +
                '}';
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
