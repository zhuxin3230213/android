package cn.gmuni.sc.module.score;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.model.ComputerScoreModel;

public class ComputerGradeScoreResultActivity extends BaseCommonActivity {

    @BindView(R.id.score_examinee_name)
    TextView examNameText;

    @BindView(R.id.score_subject_str)
    TextView examSubjectText;

    @BindView(R.id.score_id_num)
    TextView idText;

    @BindView(R.id.score_result_str)
    TextView resultText;

    @BindView(R.id.score_query_series_num)
    TextView seriesNumText;


    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_computer_grade_score_result);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ComputerScoreModel model = (ComputerScoreModel) bundle.getSerializable("score");
        examNameText.setText(model.getName());
        examSubjectText.setText(model.getType());
        idText.setText(model.getIdNum());
        resultText.setText(model.getGrade());
        seriesNumText.setText(model.getSeriesNum());
    }

    @Override
    public int getToolbar() {
        return R.id.score_computer_result_toolbar;
    }
}
