package cn.gmuni.sc.module.me.message.holder;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import cn.gmuni.sc.R;
import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.base.BaseActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.MessageModel;
import cn.gmuni.sc.module.me.MeFragment;
import cn.gmuni.sc.module.me.message.MessageActivity;
import cn.gmuni.sc.module.me.message.MessageIntFeeActivity;
import cn.gmuni.sc.module.me.message.MessageNoActivity;
import cn.gmuni.sc.module.me.message.fragment.MessageFragment;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.IMessageApi;
import cn.gmuni.sc.utils.DateUtils;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.StringUtil;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;

/**
 * @Author:ZhuXin
 * @Description:
 * @Date:Create 2018/11/21 13:37
 * @Modified By:
 **/
public class MessageModelViewHolder extends BaseViewHolder<MessageModel> {

    TextView message_all_publish_time; //时间
    TextView message_all_type_name; //类型名称
    TextView message_all_circular_round; //标记原点
    TextView message_all_state; //标记状态
    TextView message_all_user_info_name; //用户姓名
    TextView message_all_content_detail; //正文
    LinearLayout message_all_detail; //详情


    public MessageModelViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(MessageModel model, int position, MultiTypeAdapter adapter) {
        message_all_publish_time = (TextView) getView(R.id.message_all_publish_time);
        message_all_type_name = (TextView) getView(R.id.message_all_type_name);
        message_all_circular_round = (TextView) getView(R.id.message_all_circular_round);
        message_all_state = (TextView) getView(R.id.message_all_state);
        message_all_user_info_name = (TextView) getView(R.id.message_all_user_info_name);
        message_all_content_detail = (TextView) getView(R.id.message_all_content_detail);
        message_all_detail = (LinearLayout) getView(R.id.message_all_detail);

        if ("0".equals(model.getState())) {
            message_all_circular_round.setVisibility(View.VISIBLE);
            Resources resource = (Resources) message_all_state.getContext().getResources();
            ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.colorGradientEnd);
            if (csl != null) {
                message_all_state.setTextColor(csl);
            }
            //通知消息
            if ("0".equals(model.getTypeName())) {
                //今天
                if (DateUtils.getCurrDate().equals(model.getTime().substring(0, 10))) {
                    message_all_publish_time.setText(model.getTime().substring(10, 16));
                } else if (DateUtils.befoDay().equals(model.getTime().substring(0, 10))) {
                    //昨天
                    message_all_publish_time.setText("昨天" + model.getTime().substring(10, 16));
                } else {
                    //昨天以前
                    message_all_publish_time.setText(model.getTime().substring(0, 4) + "年" + model.getTime().substring(5, 7)
                            + "月" + model.getTime().substring(8, 10) + "日" + model.getTime().substring(10, 16));
                }
                message_all_type_name.setText("通知");
                message_all_user_info_name.setText("姓名:" + model.getUserInfo());
                message_all_content_detail.setText(model.getContent());
                //进入通知详情
                message_all_detail.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        Intent intent = new Intent(message_all_detail.getContext(), MessageNoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("message", model);
                        intent.putExtras(bundle);
                        ((MessageActivity) message_all_detail.getContext()).startActivityForResult(intent, MessageActivity.AFTER_CLICK_STATE);

                        //点击后消息阅读状态更改
                        Map<String, String> map = new HashMap<>();
                        map.put("id", model.getId());
                        map.put("type", model.getTypeName());
                        refeshMessageState(map);
                    }
                });
            } else if ("1".equals(model.getTypeName())) {
                //网络缴费消息
                //今天
                if (DateUtils.getCurrDate().equals(model.getTime().substring(0, 10))) {
                    message_all_publish_time.setText(model.getTime().substring(10, 16));
                } else if (DateUtils.befoDay().equals(model.getTime().substring(0, 10))) {
                    //昨天
                    message_all_publish_time.setText("昨天" + model.getTime().substring(10, 16));
                } else {
                    //昨天以前
                    message_all_publish_time.setText(model.getTime().substring(0, 4) + "年" + model.getTime().substring(5, 7)
                            + "月" + model.getTime().substring(8, 10) + "日" + model.getTime().substring(10, 16));
                }
                message_all_type_name.setText("网络缴费成功");
                message_all_user_info_name.setText("姓名:" + model.getUserInfo());
                if (!StringUtil.isEmpty(model.getNetId())) {
                    message_all_content_detail.setText("缴费账号:" + model.getNetId());
                } else {
                    message_all_content_detail.setText("缴费账号:" + model.getStudentCode());
                }
                //进入缴费详情
                message_all_detail.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        Intent intent = new Intent(message_all_detail.getContext(), MessageIntFeeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("message", model);
                        intent.putExtras(bundle);
                        ((MessageActivity) message_all_detail.getContext()).startActivityForResult(intent, MessageActivity.AFTER_CLICK_STATE);

                        //点击后消息阅读状态更改
                        Map<String, String> map = new HashMap<>();
                        map.put("id", model.getId());
                        map.put("type", model.getTypeName());
                        refeshMessageState(map);
                    }
                });
            } else {
            }
        } else {
            message_all_circular_round.setVisibility(View.GONE);
            Resources resource = (Resources) message_all_state.getContext().getResources();
            ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.fontColorMinor);
            if (csl != null) {
                message_all_state.setTextColor(csl);
            }
            //通知消息
            if ("0".equals(model.getTypeName())) {
                //今天
                if (DateUtils.getCurrDate().equals(model.getTime().substring(0, 10))) {
                    message_all_publish_time.setText(model.getTime().substring(10, 16));
                } else if (DateUtils.befoDay().equals(model.getTime().substring(0, 10))) {
                    //昨天
                    message_all_publish_time.setText("昨天" + model.getTime().substring(10, 16));
                } else {
                    //昨天以前
                    message_all_publish_time.setText(model.getTime().substring(0, 4) + "年" + model.getTime().substring(5, 7)
                            + "月" + model.getTime().substring(8, 10) + "日" + model.getTime().substring(10, 16));
                }
                message_all_state.setText("已读");
                message_all_type_name.setText("通知");
                message_all_user_info_name.setText("姓名:" + model.getUserInfo());
                message_all_content_detail.setText(model.getContent());
                //进入通知详情
                message_all_detail.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        Intent intent = new Intent(message_all_detail.getContext(), MessageNoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("message", model);
                        intent.putExtras(bundle);
                        message_all_detail.getContext().startActivity(intent);
                    }
                });
            } else if ("1".equals(model.getTypeName())) {
                //今天
                if (DateUtils.getCurrDate().equals(model.getTime().substring(0, 10))) {
                    message_all_publish_time.setText(model.getTime().substring(10, 16));
                } else if (DateUtils.befoDay().equals(model.getTime().substring(0, 10))) {
                    //昨天
                    message_all_publish_time.setText("昨天" + model.getTime().substring(10, 16));
                } else {
                    //昨天以前
                    message_all_publish_time.setText(model.getTime().substring(0, 4) + "年" + model.getTime().substring(5, 7)
                            + "月" + model.getTime().substring(8, 10) + "日" + model.getTime().substring(10, 16));
                }
                message_all_state.setText("已读");
                message_all_type_name.setText("网络缴费成功");
                message_all_user_info_name.setText("姓名:" + model.getUserInfo());
                if (!StringUtil.isEmpty(model.getNetId())) {
                    message_all_content_detail.setText("缴费账号:" + model.getNetId());
                } else {
                    message_all_content_detail.setText("缴费账号:" + model.getStudentCode());
                }
                //进入缴费详情
                message_all_detail.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        Intent intent = new Intent(message_all_detail.getContext(), MessageIntFeeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("message", model);
                        intent.putExtras(bundle);
                        message_all_detail.getContext().startActivity(intent);
                    }
                });
            } else {
            }
        }

    }


    //点击消息后阅读状态更改
    private void refeshMessageState(Map<String, String> map) {
        Network.request(Network.createApi(IMessageApi.class).updateByIdAndType(map), new Network.IResponseListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                if (data != null) {
                    String flag = String.valueOf(data.get("flag"));
                } else {
                }
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("更改阅读状态失败");
            }
        });
    }

}
