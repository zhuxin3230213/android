package cn.gmuni.sc.model.express;

import java.io.Serializable;

import cn.gmuni.sc.widget.recyclerview.model.Visitable;
import cn.gmuni.sc.widget.recyclerview.type.TypeFactory;

/**
 * @Author: zhuxin
 * @Date: 2018/9/26 18:11
 * @Description:
 */
public class CompanyIndexModel implements Serializable,Visitable {

    private String companyIndex;


    public String getCompanyIndex() {
        return companyIndex;
    }

    public void setCompanyIndex(String companyIndex) {
        this.companyIndex = companyIndex;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
