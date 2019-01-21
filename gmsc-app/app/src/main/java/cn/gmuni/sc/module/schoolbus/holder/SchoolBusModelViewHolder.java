package cn.gmuni.sc.module.schoolbus.holder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.gmuni.sc.R;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.SchoolBusModel;
import cn.gmuni.sc.module.schoolbus.SchoolBusDetailActivity;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;

/**
 * @Author:ZhuXin
 * @Description:
 * @Date:Create 2018/12/26 13:56
 * @Modified By:
 **/
public class SchoolBusModelViewHolder extends BaseViewHolder<SchoolBusModel> {


    TextView school_bus_start_campus; //始发校区
    TextView school_bus_end_campus; //终到校区
    RelativeLayout school_bus_select_re;

    public SchoolBusModelViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(SchoolBusModel model, int position, MultiTypeAdapter adapter) {
        school_bus_start_campus = (TextView) getView(R.id.school_bus_start_campus);
        school_bus_end_campus = (TextView) getView(R.id.school_bus_end_campus);
        school_bus_select_re = (RelativeLayout) getView(R.id.school_bus_select_re);
        school_bus_start_campus.setText(model.getStartCampusName());
        school_bus_end_campus.setText(model.getEndCampusName());
        school_bus_select_re.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                Intent intent = new Intent(school_bus_select_re.getContext(), SchoolBusDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("schoolBusModel", model);
                bundle.putString("weeksState",adapter.getTag().toString());
                intent.putExtras(bundle);
                school_bus_select_re.getContext().startActivity(intent);
            }
        });
    }
}
