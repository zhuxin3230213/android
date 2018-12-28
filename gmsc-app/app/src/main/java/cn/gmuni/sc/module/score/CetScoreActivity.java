package cn.gmuni.sc.module.score;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.finalteam.toolsfinal.StringUtils;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.CetScoreModel;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.IScoreApi;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.RegExUtil;
import cn.gmuni.sc.utils.StringUtil;
import cn.gmuni.sc.widget.CustomButton;
import cn.gmuni.sc.widget.CustomEditText;
import cn.gmuni.sc.widget.CustomImageView;
import cn.gmuni.sc.widget.CustomToolbar;

//测试数据
//610022181110908 邰雨歆
public class CetScoreActivity extends BaseCommonActivity {


    @BindView(R.id.score_cet_name)
    CustomEditText examNameText;

    @BindView(R.id.score_cet_card_id)
    CustomEditText examCardIdText;

    @BindView(R.id.score_cet_verify_code)
    CustomImageView verfiyCodeImage;

    @BindView(R.id.score_cet_verify_code_text)
    CustomEditText verfiyCodeText;

    @BindView(R.id.score_cet_query_btn)
    CustomButton queryBtn;

    @BindView(R.id.score_cet_warning_text)
    TextView warningText;

    private String cookie = null;



    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cet_score);


    }

    @Override
    protected void onStart() {
        super.onStart();
        initCookie();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        queryBtn.setOnClickListener(new OnMultiClickListener(){
            @Override
            public void onMultiClick(View v) {
                String name = examNameText.getText().toString();
                String cardNum = examCardIdText.getText().toString();
                String verify = verfiyCodeText.getText().toString();
                if(StringUtils.isEmpty(name)){
                    MToast.showLongToast("姓名不能为空");
                    return;
                }
                if(StringUtils.isEmpty(cardNum)){
                    MToast.showLongToast("准考证号不能为空");
                    return;
                }
                if(StringUtils.isEmpty(verify)){
                    MToast.showLongToast("验证码不能为空");
                    return;
                }
                if(!RegExUtil.test(cardNum,"^\\d{15}$")){
                    MToast.showLongToast("准考证号格式错误");
                    return;
                }
                Map<String,String> params = new HashMap<>();
                params.put("cookie",cookie);
                params.put("xm",name);
                params.put("zkzh",cardNum);
                params.put("yzm",verify);
                showLoading();
                Network.request(Network.createApi(IScoreApi.class).getCetScore(params), new Network.IResponseListener<CetScoreModel>() {
                    @Override
                    public void onSuccess(CetScoreModel data) {
                        hideLoading();
                        Intent intent = new Intent(getContext(),CetScoreResultActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("score",data);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        refreshVerfiyCode();
                    }

                    @Override
                    public void onFail(int code, String message) {
                        MToast.showShortToast(message);
                        hideLoading();
                        refreshVerfiyCode();
                    }
                });
            }
        });

        //验证码图标重新刷新
        verfiyCodeImage.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                refreshVerfiyCode();
            }
        } );
    }

    /**
     * 刷新验证码
     */
    private void refreshVerfiyCode(){
        verfiyCodeImage.reload("https://www.chsi.com.cn/cet/ValidatorIMG.JPG?ID="+(Math.random()*10000));
    }

    /**
     * 初始化页面cookies，方便查询
     */
    public void initCookie(){
        showLoading();
        Network.request(Network.createApi(IScoreApi.class).initCet(), new Network.IResponseListener<Map<String, String>>() {
            @Override
            public void onSuccess(Map<String, String> data) {
                cookie = data.get("cookie");
                warningText.setText(data.get("warning"));
                hideLoading();
                initVerfiyCode();
            }

            @Override
            public void onFail(int code, String message) {
                hideLoading();
                Logger.i(message);
            }
        });
    }

    public void initVerfiyCode(){
        verfiyCodeImage.setUrl("https://www.chsi.com.cn/cet/ValidatorIMG.JPG?ID="+(Math.random()*10000))
                .addHeader("Cookie",cookie)
                .addHeader("Referer","https://www.chsi.com.cn/cet/query?zkzh=610022181110908&xm=%E9%82%B0%E9%9B%A8%E6%AD%86&yzm=qvz")
                .addHeader("User-Agent","User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
                .reload();
    }

    @Override
    public int getToolbar() {
        return R.id.score_cet_query_toolbar;
    }
}
