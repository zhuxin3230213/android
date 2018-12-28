package cn.gmuni.sc.module.score;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.widget.listview.radiolistview.CustomRadioListView;

public class ComputerGradeSelectTimeActivity extends BaseCommonActivity {

    @BindView(R.id.cumputer_grade_select_time_listview)
    CustomRadioListView listView;

    private ArrayList<HashMap<String,Object>> times;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cumputer_grade_select_time);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        times = (ArrayList<HashMap<String, Object>>) bundle.getSerializable("times");
        init();
    }

    public void init(){
        listView.setData(times);
        toolbar.setOnActionListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                   HashMap<String,Object> time = listView.getSelectData();
                   Intent intent = new Intent();
                   intent.putExtra("examid",time.get("value").toString());
                   intent.putExtra("time",time.get("text").toString());
                   setResult(1000,intent);
                   ComputerGradeSelectTimeActivity.this.finish();
               }
        });
    }

    @Override
    public int getToolbar() {
        return R.id.score_computer_select_time_toolbar;
    }
}
