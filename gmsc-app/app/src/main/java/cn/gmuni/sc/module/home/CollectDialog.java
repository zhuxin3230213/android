package cn.gmuni.sc.module.home;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gmuni.sc.R;
import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.base.BaseActivity;
import cn.gmuni.sc.database.UserInfoDao;
import cn.gmuni.sc.model.CollegeModel;
import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.IUserInfoApi;
import cn.gmuni.sc.umpush.UmPushHelper;
import cn.gmuni.sc.utils.ClickShake;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.widget.CustomButton;
import cn.gmuni.sc.widget.listview.radiolistview.CustomRadioListView;

/**
 * 选择学校的弹出框
 */
public class CollectDialog extends Dialog {

    private CustomRadioListView listView;

    private CustomButton okBtn;

    private ImageView close;

    private ArrayList<HashMap<String ,Object>> data;

    private OnSelectedListener selectEvent;

    private Context context;

    //选中值
    private String selectId;

    //是否可关闭
    private boolean closeable;


    public CollectDialog(@NonNull Context context,String selectId) {
        super(context, R.style.CustomDialog);
        this.context = context;
        this.selectId = selectId;
        loadSchoolData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_college_dialog);
        listView = this.findViewById(R.id.college_list);
        okBtn = this.findViewById(R.id.college_ok);
        close = this.findViewById(R.id.college_close);
        listView.setData(data,this.selectId);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setCancelable(false);
        bindEvent();
    }

    /**将学生选择的学校信息进行保存*/
    private void bindEvent(){
        okBtn.setOnClickListener(v -> {
            if(!ClickShake.check(v.getId())){
                return;
            }
            HashMap<String, Object> selectData = listView.getSelectData();
            if(selectEvent!=null){
                //保存数据
                updateUserSchool(selectData);
                //回调函数
                selectEvent.onSelected(selectData);
            }
            dismiss();
        });
        close.setOnClickListener(v-> {
            if(!ClickShake.check(v.getId())){
                return;
            }
            dismiss();
        });
    }

    private void updateUserSchool(HashMap<String,Object> data){
        UserInfoDao dao = new UserInfoDao();
        UserInfoEntity entity = dao.get();
        entity.setSchool(data.get("value").toString());
        entity.setSchoolName(data.get("text").toString());
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setId(entity.getId());
        userInfoEntity.setSchool(entity.getSchool());

        Network.request(Network.createApi(IUserInfoApi.class).updateUser(userInfoEntity), new Network.IResponseListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                dao.saveOrUpdate(entity);
                SecurityUtils.updateUserInfo();
                    //为推送设置学校标签
                    UmPushHelper.getInstance().setTags(entity.getSchool());

            }

            @Override
            public void onFail(int code, String message) {

            }
        });

    }

    private void loadSchoolData(){
        BaseActivity activity = (BaseActivity) context;
        activity.showLoading();
        Network.request(Network.createApi(IUserInfoApi.class).listAll(), new Network.IResponseListener<List<CollegeModel>>() {
            @Override
            public void onSuccess(List<CollegeModel> dd) {
                activity.hideLoading();
                ArrayList<HashMap<String,Object>> dt = new ArrayList<>();
                for(CollegeModel m : dd){
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("text",m.getName());
                    map.put("value",m.getCode());
                    dt.add(map);
                }
               data = dt;
               show();
               closeable(closeable);
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("加载学校信息失败:"+message);
                activity.hideLoading();
            }
        });
    }

    /**
     * 是否可关闭
     * @param closeable
     */
    public void closeable(boolean closeable){
        this.closeable = closeable;
        if(close!=null){
            if(closeable){
                close.setVisibility(View.VISIBLE);
            }else{
                close.setVisibility(View.GONE);
            }
        }

    }

    public void setOnSelectedListener(OnSelectedListener listener){
        selectEvent = listener;
    }

    public interface OnSelectedListener{

        void  onSelected( HashMap<String, Object> data);
    }


}
