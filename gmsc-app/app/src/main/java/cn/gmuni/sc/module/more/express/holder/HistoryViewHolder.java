package cn.gmuni.sc.module.more.express.holder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.gmuni.sc.R;
import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.base.BaseActivity;
import cn.gmuni.sc.database.ExpressHistoryDao;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.entities.ExpressHistoryEntity;
import cn.gmuni.sc.model.express.CompanyModel;
import cn.gmuni.sc.model.express.HistoryModel;
import cn.gmuni.sc.module.more.express.ExpressResultActivity;
import cn.gmuni.sc.module.more.express.constants.Extras;
import cn.gmuni.sc.module.more.express.constants.RequestCode;
import cn.gmuni.sc.widget.CustomImageView;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;

/**
 * @Author: zhuxin
 * @Date: 2018/9/25 10:13
 * @Description:
 */
public class HistoryViewHolder extends BaseViewHolder<HistoryModel> {

    private CustomImageView module_express_history_company_logo;
    private TextView module_express_history_company_name;
    private TextView module_express_history_exp_number;
    private TextView module_express_history_exp_information;
    private TextView module_express_history_exp_later_time;
    private TextView module_express_history_circular_round;
    private RelativeLayout layout;
    private ImageView module_express_history_company_sign_in;
    private LinearLayout module_express_history_no_state;
    private LinearLayout module_express_history_state;
    private TextView module_express_history_check;
    private TextView module_express_history_exp_information_copy;

    public HistoryViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(HistoryModel model, int position, MultiTypeAdapter adapter) {
        module_express_history_company_logo = (CustomImageView) getView(R.id.module_express_history_company_logo);
        module_express_history_company_name = (TextView) getView(R.id.module_express_history_company_name);
        module_express_history_exp_number = (TextView) getView(R.id.module_express_history_exp_number);
        module_express_history_exp_information = (TextView) getView(R.id.module_express_history_exp_information);
        module_express_history_exp_later_time = (TextView) getView(R.id.module_express_history_exp_later_time);
        module_express_history_circular_round = (TextView) getView(R.id.module_express_history_circular_round);
        module_express_history_company_sign_in = (ImageView) getView(R.id.module_express_history_company_sign_in);
        module_express_history_no_state = (LinearLayout) getView(R.id.module_express_history_no_state);
        module_express_history_state = (LinearLayout) getView(R.id.module_express_history_state);
        module_express_history_check = (TextView) getView(R.id.module_express_history_check);
        module_express_history_exp_information_copy = (TextView) getView(R.id.module_express_history_exp_information_copy);

        layout = (RelativeLayout) getView(R.id.module_express_history_layout);
        
        if ("".equals(model.getState())) {//未签收状态
            module_express_history_exp_information.setText(model.getDetail());
        } else {//签收状态
            module_express_history_company_sign_in.setVisibility(View.VISIBLE);
            module_express_history_no_state.setVisibility(View.GONE);
            module_express_history_state.setVisibility(View.VISIBLE);
            module_express_history_check.setText(model.getState());
            module_express_history_exp_information_copy.setText(model.getDetail());
        }
        module_express_history_company_name.setText(model.getCompanyName());
        module_express_history_exp_number.setText(model.getExpNo());
        module_express_history_exp_later_time.setText(model.getCurrentDate());
        module_express_history_circular_round.setVisibility(View.GONE);
        String url = "http://www.kuaidi100.com/images/all/" + model.getCompanyLogo();
        module_express_history_company_logo.setUrl(url);
        module_express_history_company_logo.reload();


        layout.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                CompanyModel companyModel = new CompanyModel();
                companyModel.setName(model.getCompanyName());
                companyModel.setCode(model.getCompanyCode());
                companyModel.setLogo(model.getCompanyLogo());
                String expNo = model.getExpNo(); //物流单号
                Intent intent = new Intent(layout.getContext(), ExpressResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("hisCache", Extras.HISTROY_CACHE);
                bundle.putString("expNo", expNo);
                bundle.putSerializable("company", companyModel);
                intent.putExtras(bundle);
                ((BaseActivity)layout.getContext()).startActivityForResult(intent, RequestCode.REQUEST_HISTROY);
            }
        });

    }
}
