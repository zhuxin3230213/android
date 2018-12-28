package cn.gmuni.sc.module.score;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.model.CetScoreModel;

public class CetScoreResultActivity extends BaseCommonActivity {

    @BindView(R.id.score_examinee_name)
    TextView examNameText;

    @BindView(R.id.score_school_info)
    TextView schoolText;

    @BindView(R.id.score_exam_type_info)
    TextView examTypeText;

    @BindView(R.id.score_examination_write_card_num)
    TextView writeCardNumText;

    @BindView(R.id.score_write_score)
    TextView writScoreText;

    @BindView(R.id.score_listening_score)
    TextView listeningText;

    @BindView(R.id.score_reading_score)
    TextView readingText;

    @BindView(R.id.score_writing_score)
    TextView writingText;

    @BindView(R.id.score_examination_oral_card_num)
    TextView oralCardNumText;

    @BindView(R.id.score_query_oral_grade)
    TextView oralGradeText;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cet_score_result);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        CetScoreModel score = (CetScoreModel) bundle.getSerializable("score");
        examNameText.setText(score.getName());
        schoolText.setText(score.getSchool());
        examTypeText.setText(score.getType());
        writeCardNumText.setText(score.getWriteCardNum());
        writScoreText.setText(score.getWriteScore());
        listeningText.setText(score.getListeningScore());
        readingText.setText(score.getReadingScore());
        writingText.setText(score.getWritingScore());
        oralCardNumText.setText(score.getOralCardNum());
        oralGradeText.setText(score.getOralGrade());
    }

    @Override
    public int getToolbar() {
        return R.id.score_cet_result_toolbar;
    }
}
