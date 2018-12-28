package cn.gmuni.sc.module.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.module.schedule.widget.SemesterSelectWidget;

public class SemesterSelectActivity extends BaseCommonActivity {
    @BindView(R.id.semester_select_main_container)
    LinearLayout mainContainer;

    //组件缓存器
    Map<String, SemesterSelectWidget> cache = new HashMap<>();

    /**
     * 存放学期的图片id
     * 动态渲染用
     */
    private Map<Integer, Integer> imgMap = new HashMap<>();
    {
        imgMap.put(0, R.drawable.semester_select_image_01);
        imgMap.put(1, R.drawable.semester_select_image_02);
        imgMap.put(2, R.drawable.semester_select_image_03);
        imgMap.put(3, R.drawable.semester_select_image_04);
        imgMap.put(4, R.drawable.semester_select_image_05);
        imgMap.put(5, R.drawable.semester_select_image_06);
        imgMap.put(6, R.drawable.semester_select_image_07);
        imgMap.put(7, R.drawable.semester_select_image_08);
    }

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_semester_select);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        List<Map<String, String>> semesterData =  (List<Map<String, String>>)intent.getSerializableExtra("semesterData");
        Map<String, String> selectedData = (Map<String, String>)intent.getSerializableExtra("selectedData");
        if(semesterData != null && semesterData.size() > 0){
            for (int i = 0; i < semesterData.size(); i++) {
                Map<String, String> yearData = semesterData.get(i);
                String academicYear = yearData.get("academicYear");
                if(!cache.containsKey(academicYear)){
                    SemesterSelectWidget widget = new SemesterSelectWidget(getContext(), imgMap.get(cache.size() * 2), imgMap.get(cache.size() * 2 + 1), academicYear);
                    widget.setSelectListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (Map.Entry w:cache.entrySet()) {
                                ((SemesterSelectWidget)w.getValue()).unckeckAll();
                            }
                        }
                    });
                    mainContainer.addView(widget);
                    cache.put(academicYear, widget);
                }
                SemesterSelectWidget widget = cache.get(academicYear);
                widget.showWhat(yearData.get("semester"));
            }
            SemesterSelectWidget widget = cache.get(selectedData.get("academicYear"));
            widget.checkSemester(selectedData.get("semester"));
        }
        toolbar.setOnActionListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if(cache.size() > 0){
                    for (Map.Entry w:cache.entrySet()) {
                        SemesterSelectWidget widget = (SemesterSelectWidget)w.getValue();
                        if(widget.isSelectYear()){
                            Intent intent = new Intent();
                            intent.putExtra("semester",widget.getSelectSemester());
                            intent.putExtra("academicYear",widget.getAcademicYear());
                            setResult(0x11, intent);
                            finish();
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getToolbar() {
        return R.id.semester_select_toolbar;
    }

}
