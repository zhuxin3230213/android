package cn.gmuni.sc.module.more.market.holder;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gmuni.sc.R;
import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.MarketMessageModel;
import cn.gmuni.sc.model.ReMarketMessageModel;
import cn.gmuni.sc.module.more.market.marketutil.KeyBoardUtil;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.IMarketApi;
import cn.gmuni.sc.utils.BitMapUtil;
import cn.gmuni.sc.utils.JsonUtils;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.PixelUtil;
import cn.gmuni.sc.utils.StringUtil;
import cn.gmuni.sc.widget.CircleImageView;
import cn.gmuni.sc.widget.ContainsEmojiEditText;
import cn.gmuni.sc.widget.recyclerview.CustomRefreshRecycleView;
import cn.gmuni.sc.widget.recyclerview.RecycleViewDivider;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;
import cn.gmuni.sc.widget.recyclerview.model.Visitable;

/**
 * @Author:ZhuXin
 * @Description:
 * @Date:Create 2019/1/16 11:08
 * @Modified By:
 **/
public class MarketMessageModelViewHolder extends BaseViewHolder<MarketMessageModel> {

    RelativeLayout market_message_list_view_item_layout;
    CircleImageView market_message_list_item_user_img; //头像
    TextView market_message_list_item_nick_name; //昵称
    TextView market_message_list_item_update_time; //创建时间
    TextView market_message_list_item_campus; //学校
    TextView market_message_list_item_title; //留言内容
    CustomRefreshRecycleView market_message_list_view; //回复列表
    ImageView market_message_reply; //回复按钮


    LinearLayout market_operation_layout;
    LinearLayout market_message_layout;
    ContainsEmojiEditText market_message_edit;
    TextView market_message_send;

    private MultiTypeAdapter multiTypeAdapter;
    private static final List<Visitable> list = new ArrayList<>();

    public MarketMessageModelViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(MarketMessageModel model, int position, MultiTypeAdapter adapter) {
        market_message_list_view_item_layout = (RelativeLayout) getView(R.id.market_message_list_view_item_layout);

        market_operation_layout = (LinearLayout) BaseApplication.getInstance().getCurrentActivity().findViewById(R.id.market_operation_layout);
        market_message_layout = (LinearLayout) BaseApplication.getInstance().getCurrentActivity().findViewById(R.id.market_message_layout);
        market_message_edit = (ContainsEmojiEditText) BaseApplication.getInstance().getCurrentActivity().findViewById(R.id.market_message_edit);
        market_message_send = (TextView) BaseApplication.getInstance().getCurrentActivity().findViewById(R.id.market_message_send);

        market_message_list_item_user_img = (CircleImageView) getView(R.id.market_message_list_item_user_img);
        market_message_list_item_nick_name = (TextView) getView(R.id.market_message_list_item_nick_name);
        market_message_list_item_update_time = (TextView) getView(R.id.market_message_list_item_update_time);
        market_message_list_item_campus = (TextView) getView(R.id.market_message_list_item_campus);
        market_message_list_item_title = (TextView) getView(R.id.market_message_list_item_title);
        market_message_list_view = (CustomRefreshRecycleView) getView(R.id.market_message_list_view);
        market_message_reply = (ImageView) getView(R.id.market_message_reply);
        //头像
        if (!StringUtil.isEmpty(model.getAvatar())) {
            Bitmap snapshotBm = BitMapUtil.base64ToBitmap(model.getAvatar());
            market_message_list_item_user_img.setImageBitmap(snapshotBm);
        }
        market_message_list_item_nick_name.setText(model.getUserName());
        market_message_list_item_update_time.setText(model.getCreateTime());
        market_message_list_item_campus.setText(model.getSchoolName());
        market_message_list_item_title.setText(model.getMessage());
        list.clear();
        List<ReMarketMessageModel> mlist = new ArrayList<>();
        List<Map> alist = (ArrayList) model.getChildren();
        if (alist != null && alist.size() != 0) {
            for (Map temp : alist) {
                ReMarketMessageModel remarketMessageModel = JsonUtils.map2Bean(temp, ReMarketMessageModel.class);
                mlist.add(remarketMessageModel);
            }
            list.addAll(mlist);
            if (null == multiTypeAdapter) {
                multiTypeAdapter = market_message_list_view.initRecyclerView(list, market_message_list_view.getContext());
            }
            market_message_list_view.addItemDecoration(new RecycleViewDivider(0, PixelUtil.dp2px(1)));
        }


        if ("0".equals(adapter.getTag())) {
            //留言者与发布者是同一个人，可以进行回复
            market_message_reply.setVisibility(View.VISIBLE);
            market_message_reply.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    //TODO 回复功能实现
                    market_operation_layout.setVisibility(View.GONE);
                    market_message_layout.setVisibility(View.VISIBLE);
                    market_message_edit.setFocusable(true);
                    market_message_edit.setFocusableInTouchMode(true);
                    market_message_edit.requestFocus();
                    KeyBoardUtil.showInputMethod(market_message_edit.getContext()); //调用键盘
                    market_message_send.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            if (!StringUtil.isEmpty(market_message_edit.getText().toString())) {
                                market_operation_layout.setVisibility(View.VISIBLE);
                                market_message_layout.setVisibility(View.GONE);
                                KeyBoardUtil.hideKeyboard(market_message_edit); //隐藏键盘
                                addMessage(market_message_edit.getText().toString(), model.getMarketId(), model.getId()); //添加留言信息
                            }
                        }
                    });
                }
            });
        } else {
            market_message_reply.setVisibility(View.GONE);
        }

    }

    //添加留言回复信息
    private void addMessage(String message, String marketId, String id) {
        Map<String, String> map = new HashMap<>();
        map.put("marketId", marketId);
        map.put("message", message);
        map.put("replier", id);
        BaseApplication.getInstance().getCurrentActivity().showLoading();
        Network.request(Network.createApi(IMarketApi.class).addMessage(map), new Network.IResponseListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                if (data != null) {
                    MToast.showLongToast("添加回复成功");
                    //TODO 添加留言信息成功刷新列表数据
                    ReMarketMessageModel remarketMessageModel = JsonUtils.map2Bean(data, ReMarketMessageModel.class);
                    if (list.size() == 0) {
                        if (null == multiTypeAdapter) {
                            multiTypeAdapter = market_message_list_view.initRecyclerView(list, market_message_list_view.getContext());
                        }
                    }
                    list.add(0, remarketMessageModel);
                    market_message_list_view.notifyDataSetChanged();

                } else {
                    MToast.showLongToast("后台添加回复信息失败");
                }
                BaseApplication.getInstance().getCurrentActivity().hideLoading();
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showLongToast("添加回复信息失败");
                BaseApplication.getInstance().getCurrentActivity().hideLoading();
            }
        });
    }

}
