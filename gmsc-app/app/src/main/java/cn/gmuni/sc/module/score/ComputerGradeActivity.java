package cn.gmuni.sc.module.score;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.finalteam.toolsfinal.StringUtils;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.ComputerScoreModel;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.IScoreApi;
import cn.gmuni.sc.utils.ClickShake;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.widget.CustomButton;
import cn.gmuni.sc.widget.CustomEditText;
import cn.gmuni.sc.widget.CustomImageView;

//测试数据
// 凌玉婷 44162419891002382X 二级Access 2012.9
public class ComputerGradeActivity extends BaseCommonActivity {

    @BindView(R.id.score_cumputer_grade_time)
    CustomEditText timeText;

    @BindView(R.id.score_cumputer_grade_sub)
    CustomEditText subjectTex;

    @BindView(R.id.score_cumputer_grade_name)
    CustomEditText nameText;

    @BindView(R.id.score_examination_card_num)
    CustomEditText cardNumText;

    @BindView(R.id.score_computer_verify_code)
    CustomImageView verifyCodeImage;

    @BindView(R.id.score_computer_verify_code_btn)
    CustomButton vierfyCodeBtn;

    @BindView(R.id.score_computer_verify_code_text)
    CustomEditText verifyCodeText;

    @BindView(R.id.score_computer_query_btn)
    CustomButton queryBtn;

     //保存选择的时间
     private ArrayList<HashMap<String,String>> times = null;

     //保存选择的科目
     private Map<String,ArrayList<HashMap<String,Object>>> subjects = new HashMap<>();

     //保存cookies
     private String cookies = null;

     //考试时间id
     private String examid = null;

     //考试时间
     private String time = null;

     private String subId = null;

     private String subText = null;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cumputer_grade);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        /**
         * 禁止启用软键盘
         */
        timeText.setFocusable(false);
        subjectTex.setFocusable(false);
        //绑定事件
        bindEvent();
        //加载考试时间
        loadTime();

    }

    private void bindEvent(){
        timeText.setOnClickListener(view -> {
            if (!ClickShake.check(view.getId())){
                return;
            }
            Intent intent = new Intent(getContext(),ComputerGradeSelectTimeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("times",times);
            intent.putExtras(bundle);
            startActivityForResult(intent,1000);
        });

        subjectTex.setOnClickListener(view -> {
            if (!ClickShake.check(view.getId())){
                return;
            }
            if(examid!=null){
                Intent intent = new Intent(getContext(),ComputerGradeSelectSubjectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("subjects",subjects.get(examid));
                intent.putExtras(bundle);
                startActivityForResult(intent,1001);
            }else {
                //TODO
            }

        });

        queryBtn.setOnClickListener(view -> {
            if (!ClickShake.check(view.getId())){
                return;
            }
            if(examid!=null && subId!=null){
                String name = nameText.getText().toString();
                String cardNum = cardNumText.getText().toString();
                String verifyCode = verifyCodeText.getText().toString();
                if(name!=null && cardNum!=null & verifyCode!=null){
                    Map<String,String> params = new HashMap<>();
                    params.put("ksxm","300");
                    params.put("pram","certi");
                    params.put("ksnf",examid);
                    params.put("bkjb",subId);
                    params.put("sfzh",cardNum);
                    params.put("name",name);
                    params.put("verify",verifyCode);
                    params.put("cookie",cookies);
                    if(StringUtils.isEmpty(name)){
                        MToast.showLongToast("姓名不能为空");
                        return;
                    }
                    if(StringUtils.isEmpty(cardNum)){
                        MToast.showLongToast("");
                    }
                    showLoading();
                    Network.request(Network.createApi(IScoreApi.class).getCumputerScore(params), new Network.IResponseListener<ComputerScoreModel>() {
                        @Override
                        public void onSuccess(ComputerScoreModel data) {
                            data.setTime(time);
                            data.setType(subText);
                            Intent intent = new Intent(getContext(),ComputerGradeScoreResultActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("score",data);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            hideLoading();
                        }

                        @Override
                        public void onFail(int code, String message) {
                            hideLoading();
                            MToast.showShortToast(message);
                        }
                    });
                }
            }
        });

        vierfyCodeBtn.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                verifyCodeImage.reload("http://search.neea.edu.cn/Imgs.do?act=verify&t="+Math.random());
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1000){
            time = data.getStringExtra("time");
            examid = data.getStringExtra("examid");
            timeText.setText(time);
            //加载考试科目
            loadSubject();
        }
        if(requestCode==1001){
            subId  = data.getStringExtra("value");
            subText = data.getStringExtra("text");
            subjectTex.setText(subText);
        }

    }

    private void loadTime(){

        showLoading();
        Network.request(Network.createApi(IScoreApi.class).initComputer(), new Network.IResponseListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                hideLoading();
                ArrayList<HashMap<String,String>> ts = (ArrayList<HashMap<String,String>>) data.get("times");
                times = new ArrayList<>();
                for(HashMap<String,String> tree : ts){
                    HashMap<String,String> map = new HashMap<>();
                    map.put("value",tree.get("value"));
                    map.put("text",tree.get("text"));
                    times.add(map);
                }
               cookies = (String) data.get("cookie");
               timeText.setText(times.get(0).get("text"));
               examid = times.get(0).get("value");
               time = times.get(0).get("text");
                loadSubject();
                addVerfiyCode();
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast(message);
                hideLoading();
            }
        });
    }


    /**
     * 加载考试科目
     */
    private void loadSubject(){
        if(examid==null){
            //TODO
            return;
        }
        Map<String,String> params = new HashMap<>();
        params.put("examid",examid);
        if(subjects.get("examid")!=null){

        }else{
            showLoading();
            Network.request(Network.createApi(IScoreApi.class).listComputerSubject(params), new Network.IResponseListener<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    hideLoading();
                    ArrayList<HashMap<String,Object>> dt = new ArrayList<>();
                    ArrayList<HashMap<String,Object>> subs = (ArrayList<HashMap<String, Object>>) data.get("bkjbList");
                    for(HashMap<String,Object> tree : subs){
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("value",tree.get("value"));
                        map.put("text",tree.get("text"));
                       dt.add(map);
                    }
                    subjects.put(examid,dt);
                    HashMap<String,Object> map = dt.get(0);
                    subjectTex.setText(map.get("text").toString());
                    subId = map.get("value").toString();
                    subText = map.get("text").toString();
                }

                @Override
                public void onFail(int code, String message) {
                    hideLoading();
                }
            });
        }

    }

    public void addVerfiyCode(){
        verifyCodeImage.setUrl("http://search.neea.edu.cn/Imgs.do?act=verify&t="+Math.random())
                .addHeader("Cookie",cookies)
                .addHeader("Referer","http://search.neea.edu.cn/QueryMarkUpAction.do?act=doQueryResults")
                .reload();
    }

    @Override
    public int getToolbar() {
        return R.id.score_computer_query_toolbar;
    }
}
