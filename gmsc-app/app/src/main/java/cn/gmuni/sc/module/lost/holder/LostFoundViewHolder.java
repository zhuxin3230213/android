package cn.gmuni.sc.module.lost.holder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gmuni.sc.R;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.LostFoundModel;
import cn.gmuni.sc.module.lost.LostFoundDetailsActivity;
import cn.gmuni.sc.module.lost.LostFoundMineActivity;
import cn.gmuni.sc.module.lost.menu.PopupMenuLost;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.ILostFoundApi;
import cn.gmuni.sc.utils.BitMapUtil;
import cn.gmuni.sc.utils.DateUtils;
import cn.gmuni.sc.utils.PixelUtil;
import cn.gmuni.sc.utils.StringUtil;
import cn.gmuni.sc.widget.CircleImageView;
import cn.gmuni.sc.widget.CustomDialog;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;
import cn.gmuni.sc.widget.recyclerview.model.Visitable;

public class LostFoundViewHolder extends BaseViewHolder<LostFoundModel> {

    private TextView titleText;

    private TextView contentText;

    private TextView addressText;

    private TextView timeText;

    private TextView userText;

    private ImageView typeImg;

    private LinearLayout contentLayout;

    private ImageView finishFlag;

//    private TextView ifFinish;

    private ImageView deleteImg;

    private CustomDialog dialog;
    private CircleImageView userImg;

    public LostFoundViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(LostFoundModel model, int position, MultiTypeAdapter adapter) {
        titleText = (TextView) getView(R.id.lost_found_list_item_title);
        contentText = (TextView) getView(R.id.lost_found_list_item_content_text);
        addressText = (TextView) getView(R.id.lost_found_list_item_address_text);
        timeText = (TextView) getView(R.id.lost_found_list_item_time_text);
        userText = (TextView) getView(R.id.lost_found_list_item_user_text);
//        ifFinish = (TextView) getView(R.id.lost_found_list_item_change_status);
        typeImg = (ImageView) getView(R.id.lost_found_list_item_type_img);
        contentLayout = (LinearLayout) getView(R.id.lost_found_list_item_content_layout);
        finishFlag = (ImageView) getView(R.id.lost_found_list_item_finish_flag);
        deleteImg = (ImageView) getView(R.id.lost_found_list_item_delete_img);
        userImg = (CircleImageView) getView(R.id.lost_found_list_item_user_img);
        titleText.setText(model.getObjName());
        Context context = contentLayout.getContext();
        ImageView snapshot = (ImageView)getView(R.id.lost_found_list_item_content_snapshot);
        if ("0".equals(model.getInfoStatus())){
            finishFlag.setVisibility(View.INVISIBLE);
        }else{
            finishFlag.setVisibility(View.VISIBLE);
        }
        if (!StringUtil.isEmpty(model.getSnapshot())) {
            if(null == snapshot){
                snapshot = new ImageView(titleText.getContext());
                snapshot.setId(R.id.lost_found_list_item_content_snapshot);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(PixelUtil.dp2px(85), PixelUtil.dp2px(65));
                layoutParams.setMargins(PixelUtil.dp2px(10), 0, PixelUtil.dp2px(10), 0);
                snapshot.setLayoutParams(layoutParams);
            }
            Bitmap snapshotBm = BitMapUtil.base64ToBitmap(model.getSnapshot());
            snapshot.setImageBitmap(snapshotBm);
            if(contentLayout.getChildCount() == 1){
                contentLayout.addView(snapshot);
            }
        }else{
            if(null != snapshot){
                contentLayout.removeView(snapshot);
            }
        }
        String modelId = model.getId();
        Map<String, String> params = new HashMap<>();
        params.put("id",modelId);
        if(context instanceof LostFoundMineActivity){
            LostFoundMineActivity act = (LostFoundMineActivity)context;
            deleteImg.setVisibility(View.VISIBLE);
            deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenuLost win = new PopupMenuLost (context);
                    win.setDeleteLintener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            if(dialog != null){
                                dialog.dismiss();
                                dialog = null;
                            }
                            dialog = new CustomDialog(context, "温馨提示", "确定删除这条失物招领状态吗？");
                            dialog.setOnOkDialogListener(new OnMultiClickListener() {
                                @Override
                                public void onMultiClick(View v) {
                                    Network.request(Network.createApi(ILostFoundApi.class).delete(params), new Network.IResponseListener<Map<String, String>>() {
                                        @Override
                                        public void onSuccess(Map<String, String> data) {
                                            List<Visitable> models = adapter.getModels();
                                            for (int i = 0; i < models.size(); i++) {
                                                LostFoundModel m = (LostFoundModel)models.get(i);
                                                if(modelId.equals(m.getId())){
                                                    models.remove(i);
                                                    if(context instanceof LostFoundMineActivity){
                                                        ((LostFoundMineActivity) context).addDeletedId(modelId);
                                                        if(models.size() == 0){
                                                            ((LostFoundMineActivity) context).showEmpty();
                                                        }
                                                    }
                                                    adapter.notifyDataSetChanged();
                                                    break;
                                                }
                                            }
                                        }
                                        @Override
                                        public void onFail(int code, String message) {
                                            Toast.makeText(context, "删除失败，请重试", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    dialog.close();
                                }
                            });
                            dialog.setOnCancelDialogListener(new OnMultiClickListener() {
                                @Override
                                public void onMultiClick(View v) {
                                    dialog.close();
                                }
                            });
                            dialog.show();
                        }
                    });
                    if ("0".equals(model.getInfoStatus())) {
                        win.setFinishLintener(new OnMultiClickListener() {
                            @Override
                            public void onMultiClick(View v) {

                                if(dialog != null){
                                    dialog.dismiss();
                                    dialog = null;
                                }
                                dialog = new CustomDialog(context, "温馨提示", "是否确认该条招领信息已完成？");
                                dialog.setOnOkDialogListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View v) {
                                        Network.request(Network.createApi(ILostFoundApi.class).finish(params), new Network.IResponseListener<Map<String, String>>() {
                                            @Override
                                            public void onSuccess(Map<String, String> data) {
                                                model.setInfoStatus("1");
                                                if(context instanceof LostFoundMineActivity){
                                                    ((LostFoundMineActivity) context).addFinishedId(modelId);
                                                }
                                                adapter.notifyDataSetChanged();
                                            }
                                            @Override
                                            public void onFail(int code, String message) {
                                                Toast.makeText(context, "修改失败，请重试", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        dialog.close();
                                    }
                                });
                                dialog.setOnCancelDialogListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View v) {
                                        dialog.close();
                                    }
                                });
                                dialog.show();
                            }
                        });
                    }else{
                        win.inVisiableFinish();
                    }
                    win.showAsDropDown(deleteImg);
                    act.findViewById(R.id.lost_found_mine_mask).setVisibility(View.VISIBLE);
                }
            });
        }else{
            deleteImg.setVisibility(View.INVISIBLE);
//            ifFinish.setVisibility(View.INVISIBLE);
        }
        contentText.setText(model.getDescription());
        addressText.setText(model.getObjAddress());
        timeText.setText(DateUtils.date2String(model.getObjTime(), "yyyy-MM-dd"));
        userText.setText(model.getUserInfo());
        if(!StringUtil.isEmpty(model.getUserImg())){
            Bitmap snapshotBm = BitMapUtil.base64ToBitmap(model.getUserImg());
            userImg.setImageBitmap(snapshotBm);
        }else{
            userImg.setImageResource(R.drawable.default_avatar);
        }
        if ("1".equals(model.getLfType())) {
            typeImg.setImageResource(R.drawable.image_found);
        } else {
            typeImg.setImageResource(R.drawable.image_loat);
        }
        getView(R.id.lost_found_list_item_container).setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                Network.request(Network.createApi(ILostFoundApi.class).getOne(params), new Network.IResponseListener<LostFoundModel>() {
                    @Override
                    public void onSuccess(LostFoundModel data) {
                        Intent intent = new Intent(context,LostFoundDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("info",data);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                    @Override
                    public void onFail(int code, String message) {
                        if(code == 1000){
                            Toast.makeText(context, "数据不存在，可能已被删除", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(context, "查询数据失败，请重试", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

}
