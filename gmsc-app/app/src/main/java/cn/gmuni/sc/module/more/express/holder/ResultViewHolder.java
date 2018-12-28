package cn.gmuni.sc.module.more.express.holder;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.model.express.ResultModel;
import cn.gmuni.sc.widget.recyclerview.CustomRefreshRecycleView;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;

/**
 * @Author: zhuxin
 * @Date: 2018/9/20 17:54
 * @Description:
 */
public class ResultViewHolder extends BaseViewHolder<ResultModel> {


    private TextView module_express_result_item_time;

    private TextView module_express_result_item_date;

    private TextView module_express_result_detail;

    private View module_express_result_item_divider_line;


    public ResultViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(ResultModel model, int position, MultiTypeAdapter adapter) {
        module_express_result_item_time = (TextView) getView(R.id.module_express_result_item_time);
        module_express_result_item_date = (TextView) getView(R.id.module_express_result_item_date);
        module_express_result_detail = (TextView) getView(R.id.module_express_result_detail);
        module_express_result_item_divider_line = (View) getView(R.id.module_express_result_item_divider_line);

        if (0 == position) {//首条item
            Resources resource = (Resources) itemView.getContext().getResources();
            ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.fontColorPrimary);
            module_express_result_item_time.setTextColor(csl);
            module_express_result_detail.setTextColor(csl);
        }else {
            //因为recyclerView是循环利用ViewHolder的，第12个position应该是复用0位置的ViewHolder，
            // 如果不是0位置则会如何的判断，故这里会显示和0位置同样的样式，因此需要加入else判断
            Resources resource = (Resources) itemView.getContext().getResources();
            ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.fontColorMinor);
            module_express_result_item_time.setTextColor(csl);
            module_express_result_detail.setTextColor(csl);
        }
        if (adapter.getItemCount() - 1 == position) {//最后一条item
            module_express_result_item_divider_line.setVisibility(View.GONE);
        }

        module_express_result_item_time.setText(model.getCurrentTime());
        module_express_result_item_date.setText(model.getCurrentDate());
        module_express_result_detail.setText(model.getDetail());

    }
}
