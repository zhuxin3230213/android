package cn.gmuni.sc.module.score;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.model.PutonghuaModel;

/**
 * 普通话查询结果
 */
public class PutonghuaScoreResultActivity extends BaseCommonActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //覆盖默认的动画
        overridePendingTransition(R.anim.default_enteranim,R.anim.default_exitanim);
    }

    //考生姓名
    @BindView(R.id.score_examinee_name)
    TextView examineeName;

    //考生性别
    @BindView(R.id.score_examinee_sex)
    TextView examineeSex;

    //准考证号
    @BindView(R.id.score_examination_card_num)
    TextView examinationCard;

    //身份证号
    @BindView(R.id.score_id_num)
    TextView idNum;

    //分数
    @BindView(R.id.score_query_score)
    TextView score;

    //等级
    @BindView(R.id.score_query_grade_result)
    TextView grade;

    //证书编号
    @BindView(R.id.score_query_series_num)
    TextView seriesNum;

    //标题
    @BindView(R.id.score_result_title_text)
    TextView title;
    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_putonghua_score_result);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        PutonghuaModel model  = (PutonghuaModel) bundle.getSerializable("model");
        title.setText(model.getTime() + getResources().getString(R.string.score_result_info));
        examineeName.setText(model.getName());
        examineeSex.setText(model.getSex());
        examinationCard.setText(model.getExamCardNum());
        idNum.setText(model.getIdNum());
        score.setText(model.getScore());
        grade.setText(model.getGrade());
        seriesNum.setText(model.getSeriesNum());
    }

    @Override
    public int getToolbar() {
        return R.id.score_putonghua_result_toolbar;
    }
}
