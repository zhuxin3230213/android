package cn.gmuni.sc.module.score;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.VideoView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.PutonghuaModel;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.IScoreApi;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.widget.CustomButton;
import cn.gmuni.sc.widget.CustomEditText;

//测试数据
//350823199004054946
//黄小凤
public class PutonghuaScoreActivity extends BaseCommonActivity {

    @BindView(R.id.score_name)
    CustomEditText nameEditText;

    @BindView(R.id.score_num)
    CustomEditText numEditText;

    @BindView(R.id.scrore_putonghua_radiogroup)
    RadioGroup group;


    @BindView(R.id.score_query_btn)
    CustomButton queryBtn;

//    @BindView(R.id.loading)
//    VideoView videoView;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_putonghua_score);

    }

    @Override
    public void initView(Bundle savedInstanceState) {
//        Uri uri =  Uri.parse("android.resource://" + getPackageName() + "/raw/" + R.raw.loading);
//        videoView.setVideoURI(uri);
//        videoView.start();
        queryBtn.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {

                Map<String,String> params = new HashMap<>();
                params.put("name",nameEditText.getText().toString());
                int checkedRadioButtonId = group.getCheckedRadioButtonId();
                if(checkedRadioButtonId == R.id.scrore_putonghua_radio_id_num){
                    params.put("idCard",numEditText.getText().toString());
                }else{
                    params.put("stuID",numEditText.getText().toString());
                }

                showLoading();
                Network.request(Network.createApi(IScoreApi.class).getPutonghuaScore(params), new Network.IResponseListener<PutonghuaModel>() {
                    @Override
                    public void onSuccess(PutonghuaModel data) {
                        hideLoading();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("model",data);
                        Intent intent = new Intent(getContext(),PutonghuaScoreResultActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onFail(int code, String message) {
                        MToast.showLongToast(message);
                        hideLoading();
                    }
                });

            }
        });
    }

    @Override
    public int getToolbar() {
        return R.id.score_putonghua_query_toolbar;
    }
}
