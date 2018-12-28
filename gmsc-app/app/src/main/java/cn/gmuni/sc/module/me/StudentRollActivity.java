package cn.gmuni.sc.module.me;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.toolsfinal.StringUtils;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.database.UserInfoDao;
import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.ILoginApi;
import cn.gmuni.sc.utils.ClickShake;
import cn.gmuni.sc.utils.DesUtils;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.utils.StringUtil;
import cn.gmuni.sc.widget.CustomButton;
import cn.gmuni.sc.widget.CustomEditText;

public class StudentRollActivity extends BaseCommonActivity {

    @BindView(R.id.me_roll_school)
    CustomEditText schoolText;

    @BindView(R.id.me_roll_name)
    CustomEditText nameText;

    @BindView(R.id.me_roll_student_id)
    CustomEditText studentIdText;

    @BindView(R.id.me_roll_password)
    CustomEditText passwordText;

    @BindView(R.id.me_roll_submit)
    CustomButton submit;

    @BindView(R.id.me_roll_password_wrap)
    LinearLayout passwordWrap;

    @BindView(R.id.me_roll_info)
    TextView infoText;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_student_roll);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        UserInfoEntity entity = SecurityUtils.getUserInfo();
        boolean isPreview = entity.getStudentId()!=null && !"".equals(entity.getStudentId());
        //添加学校
        schoolText.setText(entity.getSchoolName());
        infoText.setVisibility(View.GONE);
        //如果是预览，所有选项都禁止输入
        if(isPreview){
            preview(entity);
        }
    }

    private void preview( UserInfoEntity entity){
        nameText.setText(entity.getRealName());
        studentIdText.setText(entity.getStudentId());
        nameText.setFocusable(false);
        studentIdText.setFocusable(false);
        passwordWrap.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        infoText.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.me_roll_submit)
    protected void onSubmit(View view){
        if(!ClickShake.check(view.getId())){
            return;
        }
        String studentId = studentIdText.getText().toString();
        String realName = nameText.getText().toString();
        String password = passwordText.getText().toString();
        if(StringUtils.isEmpty(studentId)){
            MToast.showShortToast("学号不能为空");
            return;
        }
        if(StringUtils.isEmpty(realName)){
            MToast.showShortToast("姓名不能为空");
            return;
        }
        if(StringUtils.isEmpty(password)){
            MToast.showShortToast("密码不能为空");
            return;
        }
        Map<String,String> params = new HashMap<>();
        params.put("username",studentId);
        params.put("password", DesUtils.encode(password,studentId));
        params.put("realName",realName);
        Network.request(Network.createApi(ILoginApi.class).loginEndPoint(params), new Network.IResponseListener<Map<String,Object>>() {
            @Override
            public void onSuccess(Map<String,Object> data) {
                //如果登录成功
                if((Boolean) data.get("success")){
                    UserInfoDao dao = new UserInfoDao();
                    UserInfoEntity entity = dao.get();
                    entity.setRealName(realName);
                    entity.setStudentId(studentId);
                    dao.saveOrUpdate(entity);
                    SecurityUtils.updateUserInfo();
                    //登录成功后进入预览界面
                    preview(entity);
                }else{
                    String msg = (String) data.get("msg");
                    if("nameOrPwdIncorrect".equals(msg)){
                        MToast.showLongToast("学号或密码错误");
                    }else{
                        MToast.showLongToast("登录失败");
                    }
                }
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast(message);
            }
        });

    }

    @Override
    public int getToolbar() {
        return R.id.me_roll_query_toolbar;
    }
}
