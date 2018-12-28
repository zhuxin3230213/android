package cn.gmuni.sc.module.me.personal_details;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseClipImageActivity;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnPermissionListener;
import cn.gmuni.sc.config.PermissionConst;
import cn.gmuni.sc.database.UserInfoDao;
import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.module.me.MeFragment;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.IUserInfoApi;
import cn.gmuni.sc.utils.BitMapUtil;
import cn.gmuni.sc.utils.ClickShake;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.widget.CustomDialog;
import cn.gmuni.sc.widget.CustomEditText;
import cn.gmuni.sc.widget.CustomImageView;

public class PersionalDetailsActivity extends BaseCommonActivity {

    private static final String IMAGE_FILE_NAME = "temp_avatar_image.jpg";

    //请求识别码
    //本地
    private static final int CODE_GALLERY_REQUEST = 0xa0;

    //最终裁剪结果
    public static final int CODE_RESULT_REQUEST =0xa2;

    @BindView(R.id.me_details_name        )
    TextView nameView;

    @BindView(R.id.me_details_school)
    TextView schoolView;

    @BindView(R.id.me_details_sex)
    TextView sexView;

    @BindView(R.id.me_details_phone_number)
    TextView phoneNumberView;

    @BindView(R.id.me_details_student_id)
    TextView studentIdView;

    @BindView(R.id.me_persion_details_avatar)
    CustomImageView avatarView;

    //记录是否修改的数据
    private boolean isUpdateData = false;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_persional_details);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        UserInfoEntity entity = SecurityUtils.getUserInfo();
        //回填数据
        schoolView.setText(null!=entity.getSchool()?entity.getSchoolName():"");
        nameView.setText(null!=entity.getName()?entity.getName():"");
        sexView.setText(null!=entity.getSex()?entity.getSex():"");
        phoneNumberView.setText(entity.getPhoneNumber().replaceAll("(^\\d{3})\\d{4}(\\d{4})$","$1****$2"));
        studentIdView.setText(null!=entity.getStudentId()?entity.getStudentId():"");
        if(null!=entity.getAvatar()&&!"".equals(entity.getAvatar())){
            avatarView.setImageBitmap(BitMapUtil.base64ToBitmap(entity.getAvatar()));
        }
        if(entity.getStudentId()!=null && !"".equals(entity.getStudentId())){
            studentIdView.setText(entity.getStudentId());
        }
    }

    @Override
    public int getToolbar() {
        return R.id.me_detail_info_toolbar;
    }


    @Override
    public void beforeGoBack() {
        if(isUpdateData){
            setResult(MeFragment.UPDATE_USERINFO_CODE);
        }
    }
    //TODO 暂时不支持修改学校
    @OnClick(R.id.me_detail_school_wrap)
    protected void onUpdateSchool(View view){
        if(!ClickShake.check(view.getId())){
            return;
        }
        MToast.showShortToast("不支持切换学校");
//        CollectDialog dialog = new CollectDialog(getContext(),SecurityUtils.getUserInfo().getSchool());
//        dialog.setOnSelectedListener(d -> schoolView.setText(d.get("text").toString()));
    }


    @OnClick(R.id.me_detail_student_id_wrap)
    protected  void onUpdateStudentId(View view){
        if(!ClickShake.check(view.getId())){
            return;
        }
        MToast.showShortToast("请在学籍管理中完善学籍信息");
    }


    @OnClick(R.id.me_detail_name_wrap)
    protected  void onUpdateName(View view){
        if(!ClickShake.check(view.getId())){
            return;
        }
        CustomDialog dialog = new CustomDialog(getContext(),"输入昵称",R.layout.fragment_me_persional_user_name);
        //在加载完成内容后，赋值
        dialog.setOnLoadComparedContentListener(v ->{
            UserInfoEntity entity = SecurityUtils.getUserInfo();
            CustomEditText text = v.findViewById(R.id.me_persional_dialog_username);
            text.setText(entity.getName()!=null?entity.getName():"");
        });

        //修改密码确定
        dialog.setOnOkDialogListener(v -> {
            UserInfoEntity entity = SecurityUtils.getUserInfo();
            CustomEditText text = v.findViewById(R.id.me_persional_dialog_username);
            String name = text.getText().toString();
            entity.setName(name);
            updateUserInfo(entity, dialog, entity1 -> nameView.setText(entity1.getName()));
        });

        //修改名称取消
        dialog.setOnCancelDialogListener(v -> dialog.close());
        dialog.show();
    }

    /**
     * 修改性别
     * @param view
     */
    @OnClick(R.id.me_detail_name_sex_wrap)
    protected  void onUpdateSex(View view){
        if(!ClickShake.check(view.getId())){
            return;
        }
        CustomDialog dialog = new CustomDialog(getContext(),"选择性别",R.layout.fragment_me_persional_sex);
        dialog.setOnLoadComparedContentListener(v -> {
            UserInfoEntity entity = SecurityUtils.getUserInfo();
            ImageView man = v.findViewById(R.id.me_persional_dialog_sex_man);
            ImageView woman = v.findViewById(R.id.me_persional_dialog_sex_woman);
            if("女".equals(entity.getSex())){
                man.setImageResource(R.drawable.man_01);
                man.setTag(true);
                woman.setImageResource(R.drawable.women);
                woman.setTag(false);
            }else{
                man.setImageResource(R.drawable.man);
                man.setTag(false);
                woman.setImageResource(R.drawable.women_01);
                woman.setTag(true);
            }
            man.setOnClickListener(v1 -> {
                man.setImageResource(R.drawable.man);
                man.setTag(true);
                woman.setImageResource(R.drawable.women_01);
                woman.setTag(false);
            });

            woman.setOnClickListener(v1 -> {
                man.setImageResource(R.drawable.man_01);
                man.setTag(false);
                woman.setImageResource(R.drawable.women);
                woman.setTag(true);
            });
        });
        dialog.setOnOkDialogListener(v -> {
            ImageView man = v.findViewById(R.id.me_persional_dialog_sex_man);
            UserInfoEntity entity = SecurityUtils.getUserInfo();
            if((Boolean)man.getTag()){
                entity.setSex((String) getResources().getText(R.string.me_sex_man));
            }else{
                entity.setSex((String) getResources().getText(R.string.me_sex_woman));
            }
            updateUserInfo(entity, dialog, entity1 -> sexView.setText(entity1.getSex()));
        });

        //修改性别取消
        dialog.setOnCancelDialogListener(v -> dialog.close());
        dialog.show();
    }

    /**
     * 手机号码点击事件
     * @param view
     */
    @OnClick(R.id.me_details_phone_number_wrap)
    protected  void onClickPhoneNumber(View view){
        if(!ClickShake.check(view.getId())){
            return;
        }
        MToast.showShortToast("不支持修改手机号码");
    }

    /**
     * 更新用户信息
     * @param entity
     * @param dialog
     */
    private void updateUserInfo(UserInfoEntity entity, CustomDialog dialog, OnOkDialogCloseListener close){
        if(dialog!=null){
            dialog.close();
        }
        showLoading();
        Network.request(Network.createApi(IUserInfoApi.class).updateUser(entity), new Network.IResponseListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                hideLoading();
                UserInfoDao dao = new UserInfoDao();
                dao.saveOrUpdate(entity);
                SecurityUtils.updateUserInfo();
                if(close!=null){
                    close.onClose(entity);
                }
                isUpdateData = true;
            }

            @Override
            public void onFail(int code, String message) {

            }
        });
    }

    /**
     * 点击头像事件
     * @param view
     */
    @OnClick(R.id.me_persion_details_avatar)
    protected  void onChooseAvatar(View view){
        if(!ClickShake.check(view.getId())){
            return;
        }
        choseHeadImageFromGallery();
    }

    //从本地相册选择图片作为头像
    private void choseHeadImageFromGallery(){
        //申请读取存储空间权限
        requestPermission(PermissionConst.PERMISSION_WRITE_EXTERNAL_STORAGE, new OnPermissionListener() {
            @Override
            public void onReceive() {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,CODE_GALLERY_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        //没有进行有效设置操作，返回
        if(requestCode == RESULT_CANCELED){
            return;
        }
        switch (requestCode){
            //本地图片
            case CODE_GALLERY_REQUEST:
                if(intent==null){
                   return;
                }
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(intent.getData(), filePathColumns,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String imagePath = c.getString(columnIndex);
                Intent it = new Intent(getContext(),BaseClipImageActivity.class);
                it.putExtra("path",imagePath);
                startActivityForResult(it,CODE_RESULT_REQUEST);
                c.close();
                break;

            //裁剪完图片返回
            case CODE_RESULT_REQUEST:
                //如果intent为空，则说明直接返回，没有点击确定
                if(null == intent){
                    return;
                }
                String img = intent.getStringExtra("img");
                avatarView.setImageBitmap(BitMapUtil.base64ToBitmap(img));
                UserInfoEntity entity = SecurityUtils.getUserInfo();
                entity.setAvatar(img);

                //保存照片
                updateUserInfo(entity, null, entity1 -> {
                    avatarView.setImageBitmap(BitMapUtil.base64ToBitmap(entity.getAvatar()));
                });
                break;
        }
    }


    public interface OnOkDialogCloseListener {
        void onClose(UserInfoEntity entity);
    }

}
