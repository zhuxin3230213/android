package cn.gmuni.sc.module.more.express.holder;

import android.view.View;
import android.widget.TextView;

import cn.gmuni.sc.R;
import cn.gmuni.sc.model.express.CompanyIndexModel;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;

/**
 * @Author: zhuxin
 * @Date: 2018/9/26 18:17
 * @Description:
 */
public class ExpressCompanyIndexViewHolder extends BaseViewHolder<CompanyIndexModel> {


    private TextView express_company_index_item;

    public ExpressCompanyIndexViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(CompanyIndexModel model, int position, MultiTypeAdapter adapter) {
        express_company_index_item =(TextView) getView(R.id.express_company_index_item);
        express_company_index_item.setText(model.getCompanyIndex());
    }
}
