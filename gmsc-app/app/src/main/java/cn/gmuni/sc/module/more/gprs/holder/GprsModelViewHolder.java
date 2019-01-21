package cn.gmuni.sc.module.more.gprs.holder;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import cn.gmuni.sc.R;
import cn.gmuni.sc.model.GprsModel;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;

/**
 * @Author:ZhuXin
 * @Description:
 * @Date:Create 2018/11/30 11:15
 * @Modified By:
 **/
public class GprsModelViewHolder extends BaseViewHolder<GprsModel> {

    private TextView gpr_gprsTime;
    private TextView gprs_gprsAddress;
    private View module_gprs_item_top_divider_line;
    private TextView gprs_circular_roundad;

    public GprsModelViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(GprsModel model, int position, MultiTypeAdapter adapter) {
        gpr_gprsTime = (TextView) getView(R.id.gpr_gprsTime);
        gprs_gprsAddress = (TextView) getView(R.id.gprs_gprsAddress);
        module_gprs_item_top_divider_line = (View) getView(R.id.module_gprs_item_top_divider_line);
        gprs_circular_roundad = (TextView) getView(R.id.gprs_circular_roundad);

        if (0 == position) {//首条ite
            Resources resource = (Resources) itemView.getContext().getResources();
            Drawable drawable = resource.getDrawable(R.drawable.gprs_circular_round28);
            gprs_circular_roundad.setBackground(drawable);
            ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.fontColorGeneric);
            gpr_gprsTime.setTextColor(csl);
            Drawable drawablel = resource.getDrawable(R.color.colorNormalBackground);
            module_gprs_item_top_divider_line.setBackground(drawablel);
        } else {
            Resources resource = (Resources) itemView.getContext().getResources();
            Drawable drawable = resource.getDrawable(R.drawable.gprs_circular_roundad);
            gprs_circular_roundad.setBackground(drawable);
            ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.fontColorPrimary);
            gpr_gprsTime.setTextColor(csl);
            Drawable drawablel = resource.getDrawable(R.color.gprs_circular_round_bg);
            module_gprs_item_top_divider_line.setBackground(drawablel);
        }
        gpr_gprsTime.setText("签到时间 " + model.getGprsTime().substring(11));
        gprs_gprsAddress.setText(model.getGprsAddress());

    }
}
