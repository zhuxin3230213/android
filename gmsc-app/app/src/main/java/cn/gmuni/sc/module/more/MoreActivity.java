package cn.gmuni.sc.module.more;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.base.BaseWebViewActivity;
import cn.gmuni.sc.database.UserInfoDao;
import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.module.lost.LostFoundActivity;
import cn.gmuni.sc.module.more.express.ExpressQueryActivity;
import cn.gmuni.sc.module.more.gprs.GprsActivity;
import cn.gmuni.sc.module.news.NewsActivity;
import cn.gmuni.sc.module.pay.PayActivity;
import cn.gmuni.sc.module.schedule.ScheduleActivity;
import cn.gmuni.sc.module.schoolbus.SchoolBusActivity;
import cn.gmuni.sc.module.schoolcard.SchoolCardActivity;
import cn.gmuni.sc.module.score.ScoreActivity;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.INetPayApi;
import cn.gmuni.sc.network.api.IUserInfoApi;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.utils.StringUtil;
import cn.gmuni.sc.widget.CustomConstraintLayout;
import cn.gmuni.sc.widget.CustomDialog;

public class MoreActivity extends BaseCommonActivity {

    @BindView(R.id.more_container)
    CustomConstraintLayout container;

    @Override
    public void initContentView(Bundle savedInstanceState) {
       setContentView(R.layout.activity_more);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @OnClick({R.id.home_cell_card,R.id.home_cell_tuition_fees,R.id.home_cell_pay,
    R.id.home_cell_schedule,R.id.home_cell_score,R.id.home_cell_car,R.id.home_cell_lost_found,
    R.id.home_cell_news,
    R.id.home_train_booking,R.id.home_express_query,R.id.home_stu_location})
    public void  onClick(LinearLayout view){
           switch (view.getId()){
               case R.id.home_cell_card:
                   startActivitySecurity(new Intent(getContext(), SchoolCardActivity.class));
                   break;
               case R.id.home_cell_tuition_fees:
                   break;
               case R.id.home_cell_pay:
                   getNetInfoAndStartActivity();
                   break;
               case R.id.home_cell_schedule:
                   startActivitySecurity(new Intent(getContext(),ScheduleActivity.class));
                   break;
               case R.id.home_cell_score:
                   startActivity(new Intent(getContext(),ScoreActivity.class));
                   break;
               case R.id.home_cell_car:
                   startActivity(new Intent(getContext(), SchoolBusActivity.class));
                   break;
               case R.id.home_cell_lost_found:
                   startActivity(new Intent(getContext(),LostFoundActivity.class));
                   break;
               case R.id.home_cell_news:
                   startActivity(new Intent(getContext(),NewsActivity.class));
                   break;
               case R.id.home_train_booking:
                   Intent intent = new Intent(getContext(),BaseWebViewActivity.class);
                   Bundle bundle = new Bundle();
                   bundle.putString("url","https://m.ctrip.com/webapp/train/#/index?VNK=31c0e3a8");
                   intent.putExtras(bundle);
                   startActivity(intent);
                   break;
               case R.id.home_express_query:
                   startActivity(new Intent(getContext(),ExpressQueryActivity.class));
                   break;
               case R.id.home_stu_location:
                   startActivity(new Intent(getContext(), GprsActivity.class));
                   break;
           }
    }

    /**
     * 业务逻辑：先判断是否绑定上网账号，若未绑定提示用户输入上网账号，查询账号存在与否
     * 若已绑定，直接跳转到页面
     */
    private void getNetInfoAndStartActivity(){
        MoreActivity self = this;
        UserInfoEntity userInfo = SecurityUtils.getUserInfo();
        if(StringUtil.isEmpty(userInfo.getNetAccount())){
            CustomDialog dialog = new CustomDialog(getContext(),"输入上网账号",R.layout.fragment_pay_net_net_account);

            dialog.setOnOkDialogListener(v -> {
                EditText text = v.findViewById(R.id.pay_net_net_account);
                String account = text.getText().toString();
                Map<String, String> params = new HashMap<>();
                params.put("account", account);
                Network.request(Network.createApi(INetPayApi.class).checkNetAccountExist(params), new Network.IResponseListener<Map<String, String>>() {
                    @Override
                    public void onSuccess(Map<String, String> data) {
                        if("t".equalsIgnoreCase(data.get("exist"))){
                            UserInfoDao dao = new UserInfoDao();
                            userInfo.setNetAccount(account);
                            dao.saveOrUpdate(userInfo);
                            SecurityUtils.updateUserInfo();
                            dialog.close();
                            Network.request(Network.createApi(IUserInfoApi.class).updateUser(userInfo), new Network.IResponseListener<Map<String, Object>>() {
                                @Override
                                public void onSuccess(Map<String, Object> data) {

                                }
                                @Override
                                public void onFail(int code, String message) {

                                }
                            });
                            startActivitySecurity(new Intent(getContext(),PayActivity.class));
                        }else{
                            Toast.makeText(self, "账号输入有误", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFail(int code, String message) {
                    }
                });
            });

            dialog.setOnCancelDialogListener(v -> dialog.close());
            dialog.show();
        }else{
            startActivitySecurity(new Intent(getContext(),PayActivity.class));
        }
    }

    @Override
    public int getToolbar() {
        return R.id.more_toolbar;
    }
}
