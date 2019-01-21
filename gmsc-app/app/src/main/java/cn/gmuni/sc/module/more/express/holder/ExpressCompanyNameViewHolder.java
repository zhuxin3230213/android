package cn.gmuni.sc.module.more.express.holder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.gmuni.sc.R;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.express.CompanyListModel;
import cn.gmuni.sc.model.express.CompanyModel;
import cn.gmuni.sc.module.more.express.ExpressResultActivity;
import cn.gmuni.sc.module.more.express.constants.Extras;
import cn.gmuni.sc.widget.CustomImageView;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;

/**
 * @Author: zhuxin
 * @Date: 2018/9/26 17:17
 * @Description:
 */
public class ExpressCompanyNameViewHolder extends BaseViewHolder<CompanyListModel> {

    private CustomImageView express_company_logo;
    private TextView express_company_name;
    private LinearLayout layout;
    public ExpressCompanyNameViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(CompanyListModel model, int position, MultiTypeAdapter adapter) {

        express_company_logo = (CustomImageView) getView(R.id.express_company_logo);
        express_company_name = (TextView) getView(R.id.express_company_name);
        layout = (LinearLayout) getView(R.id.express_company_name_layout);
        express_company_name.setText(model.getName());
        String url="http://www.kuaidi100.com/images/all/"+model.getLogo();
        express_company_logo.setUrl(url);
        express_company_logo.reload();

        layout.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                String expNo = adapter.getTag().toString();
                CompanyModel company = new CompanyModel();
                company.setName(model.getName());
                company.setLogo(model.getLogo());
                company.setCode(model.getCode());
                Intent intent = new Intent(layout.getContext(), ExpressResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("hisCache", Extras.COMPANY_CACHE);
                bundle.putString("expNo",expNo);
                bundle.putSerializable("company",company);
                intent.putExtras(bundle);
                layout.getContext().startActivity(intent);
            }
        });
    }
}
