package cn.gmuni.sc.module.news.holder;

import android.view.View;
import android.widget.TextView;

import cn.gmuni.sc.R;
import cn.gmuni.sc.model.InforTipsModel;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;

/**
 * @Author: zhuxin
 * @Date: 2018/10/26 10:24
 * @Description:
 */
public class InforTipsModelViewHolder extends BaseViewHolder<InforTipsModel> {


    private TextView in_bottom_tip;

    public InforTipsModelViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(InforTipsModel model, int position, MultiTypeAdapter adapter) {
        in_bottom_tip = (TextView) getView(R.id.in_bottom_tip);
        in_bottom_tip.setText(model.getInformationTips());
    }
}
