package cn.gmuni.sc.module.more.express.holder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseWebViewActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.express.CompanyModel;
import cn.gmuni.sc.module.more.express.ExpressResultActivity;
import cn.gmuni.sc.module.more.express.constants.Extras;
import cn.gmuni.sc.widget.CustomImageView;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;

/**
 * @Author: zhuxin
 * @Date: 2018/9/20 10:11
 * @Description:
 * 单号识别快递公司列表
 */
public class SuggestionViewHolder extends BaseViewHolder<CompanyModel> {

    private CustomImageView module_express_company_logo;
    private TextView module_express_company_name;
    private ImageView module_express_company_into;  //键入

    private RelativeLayout layout;

    public SuggestionViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(final CompanyModel model, int position, final MultiTypeAdapter adapter) {
            module_express_company_logo = (CustomImageView) getView(R.id.module_express_company_logo);
            module_express_company_name = (TextView) getView(R.id.module_express_company_name);
            module_express_company_into = (ImageView) getView(R.id.module_express_company_into);

            module_express_company_name.setText(model.getName());
            String url="http://www.kuaidi100.com/images/all/"+model.getLogo();
            module_express_company_logo.setUrl(url);
            module_express_company_logo.reload();
            layout = (RelativeLayout)getView(R.id.module_express_company_layout);
            layout.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    String expNo = adapter.getTag().toString(); //物流单号
                    Intent intent = new Intent(layout.getContext(), ExpressResultActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("hisCache", Extras.SUGGEST_CACHE);
                    bundle.putString("expNo",expNo);
                    bundle.putSerializable("company",model);
                    intent.putExtras(bundle);
                    layout.getContext().startActivity(intent);
                }
            });

    }
}
