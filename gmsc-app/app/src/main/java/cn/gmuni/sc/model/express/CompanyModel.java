package cn.gmuni.sc.model.express;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import cn.gmuni.sc.widget.recyclerview.model.Visitable;
import cn.gmuni.sc.widget.recyclerview.type.TypeFactory;

/**
 * @Author: zhuxin
 * @Date: 2018/9/20 09:48
 * @Description:
 */
public class CompanyModel implements Serializable,Visitable{

    @SerializedName("name")
    private String name;
    @SerializedName("code")
    private String code;
    @SerializedName("logo")
    private String logo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
