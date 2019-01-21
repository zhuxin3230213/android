package cn.gmuni.sc.module.score;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.widget.listview.radiolistview.CustomRadioListView;

public class ComputerGradeSelectSubjectActivity extends BaseCommonActivity {


    @BindView(R.id.cumputer_grade_select_subject_listview)
    CustomRadioListView listView;

    private ArrayList<HashMap<String,Object>> subjects;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_computer_grade_select_subject);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        subjects = (ArrayList<HashMap<String, Object>>) bundle.getSerializable("subjects");
        init();
    }

    public void init(){
        listView.setData(subjects);
        toolbar.setOnActionListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                HashMap<String,Object> time = listView.getSelectData();
                Intent intent = new Intent();
                intent.putExtra("value",time.get("value").toString());
                intent.putExtra("text",time.get("text").toString());
                setResult(1000,intent);
                ComputerGradeSelectSubjectActivity.this.finish();
            }
        });
    }

    @Override
    public int getToolbar() {
        return R.id.score_computer_select_subject_toolbar;
    }
}
