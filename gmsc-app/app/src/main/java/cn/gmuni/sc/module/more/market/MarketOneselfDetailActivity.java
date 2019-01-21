package cn.gmuni.sc.module.more.market;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.MarketMessageModel;
import cn.gmuni.sc.model.MarketModel;
import cn.gmuni.sc.module.me.me_market.MeMarketActivity;
import cn.gmuni.sc.module.more.express.binding.Bind;
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
import cn.gmuni.sc.widget.CustomDialog;
import cn.gmuni.sc.widget.CustomEmptyDataView;
import cn.gmuni.sc.widget.CustomImageView;
import cn.gmuni.sc.widget.recyclerview.CustomRefreshRecycleView;
import cn.gmuni.sc.widget.recyclerview.RecycleViewDivider;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.model.Visitable;

public class MarketOneselfDetailActivity extends BaseCommonActivity {

    @BindView(R.id.market_other_oneself_list_view)
    CustomRefreshRecycleView listview;
    @BindView(R.id.market_oneself_detail_user_img)
    CircleImageView market_oneself_detail_user_img; //头像
    @BindView(R.id.market_oneself_detail_nick_name)
    TextView market_oneself_detail_nick_name; //昵称
    @BindView(R.id.market_oneself_detail_update_time)
    TextView market_oneself_detail_update_time; //更新时间
    @BindView(R.id.market_oneself_detail_campus)
    TextView market_oneself_detail_campus; //学校
    @BindView(R.id.market_oneself_detail_images)
    CustomImageView market_oneself_detail_images; //物品拍照图片（可能多个）
    @BindView(R.id.market_oneself_detail_price)
    TextView market_oneself_detail_price; //价格
    @BindView(R.id.market_oneself_detail_title)
    TextView market_oneself_detail_title; //标题
    @BindView(R.id.market_oneself_detail_introduce)
    TextView market_oneself_detail_introduce; //简介

    @BindView(R.id.market_message)
    TextView market_message; //留言
    @BindView(R.id.market_editor)
    TextView market_editor;  //编辑
    @BindView(R.id.market_delete)
    TextView market_delete; //删除
    @BindView(R.id.market_remark_sale)
    TextView market_remark_sale; //标记已出售
    @BindView(R.id.marke_oneself_count)
    TextView marke_oneself_count; //统计

    @BindView(R.id.market_operation_layout)
    LinearLayout market_operation_layout;
    @BindView(R.id.market_message_layout)
    LinearLayout market_message_layout;
    @BindView(R.id.market_message_edit)
    ContainsEmojiEditText market_message_edit;

    @BindView(R.id.market_message_send)
    TextView market_message_send;

    @BindView(R.id.market_oneself_message_no_data_empty)
    CustomEmptyDataView market_oneself_message_no_data_empty;


    private MultiTypeAdapter multiTypeAdapter;
    private static final List<Visitable> list = new ArrayList<>();
    protected final Map<String, String> params = new HashMap<>();

    private boolean isAddItemDecoration = true;
    //定义一个标志位，用于标记用户已经将列表拉到底了
    private boolean isLast = false;
    //定义一个标志位，用于标记页面正在加载数据
    private boolean isLoading = false;
    private MarketModel marketModel;
    private CustomDialog dialog;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_market_oneself_detail);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        marketModel = (MarketModel) getIntent().getSerializableExtra("marketModel");
        //头像
        if (!StringUtil.isEmpty(marketModel.getAvatar())) {
            Bitmap snapshotBm = BitMapUtil.base64ToBitmap(marketModel.getAvatar());
            market_oneself_detail_user_img.setImageBitmap(snapshotBm);
        }
        market_oneself_detail_nick_name.setText(marketModel.getUserName());
        market_oneself_detail_update_time.setText(marketModel.getUpdateTime());
        market_oneself_detail_campus.setText(marketModel.getSchoolName());
        market_oneself_detail_price.setText(marketModel.getPrice());
        market_oneself_detail_title.setText(marketModel.getTitle());
        market_oneself_detail_introduce.setText(marketModel.getIntroduce());

        getImgBData(marketModel.getId()); //获取大图
        //添加分割线
        if (isAddItemDecoration) {
            isAddItemDecoration = false;
            listview.addItemDecoration(new RecycleViewDivider(0, PixelUtil.dp2px(1)));
        }
        //刷新事件
        //listview.setOnRefreshListener(this);

        if ("1".equals(marketModel.getSaleStatus())) {
            market_editor.setVisibility(View.GONE);
            market_remark_sale.setVisibility(View.GONE);
        } else {
            market_editor.setVisibility(View.VISIBLE);
            market_remark_sale.setVisibility(View.VISIBLE);
        }
        //点击事件
        market_message.setOnClickListener(new MyListener());
        market_editor.setOnClickListener(new MyListener());
        market_delete.setOnClickListener(new MyListener());
        market_remark_sale.setOnClickListener(new MyListener());
    }

    //获取大图
    private void getImgBData(String id) {
        list.clear();
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        showLoading();
        Network.request(Network.createApi(IMarketApi.class).findById(map), new Network.IResponseListener<MarketModel>() {
            @Override
            public void onSuccess(MarketModel data) {
                if (!StringUtil.isEmpty(data.getImgB())) {
                    marketModel.setImgB(data.getImgB());
                    market_oneself_detail_images.setImageBitmap(BitMapUtil.base64ToBitmap(data.getImgB()));
                }
                //留言列表
                handleListMessage(data);
                hideLoading();
            }

            @Override
            public void onFail(int code, String message) {
                hideLoading();
            }
        });
    }

    //处理留言列表数据
    private void handleListMessage(MarketModel data) {
        if (data != null) {
            listview.setVisibility(View.VISIBLE);
            market_oneself_message_no_data_empty.setVisibility(View.GONE);
            List<MarketMessageModel> mlist = new ArrayList<>();
            List<Map> alist = (ArrayList) data.getList();
            for (Map temp : alist) {
                MarketMessageModel marketMessageModel = JsonUtils.map2Bean(temp, MarketMessageModel.class);
                mlist.add(marketMessageModel);
            }
            isLoading = false;
            isLast = (mlist == null ? true : mlist.size() < 11);
            if (mlist.size() == 11) {
                mlist.remove(10);
            }
            if (!mlist.isEmpty()) {
                list.addAll(mlist);
                if (null == multiTypeAdapter) {
                    multiTypeAdapter = listview.initRecyclerView(list, getContext());
                    multiTypeAdapter.setTag("0"); //发布者与留言者是同一个人，可以进行回复
                }
            } else {
                listview.setVisibility(View.GONE);
                market_oneself_message_no_data_empty.setVisibility(View.VISIBLE);
            }

        } else {
            listview.setVisibility(View.GONE);
            market_oneself_message_no_data_empty.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getToolbar() {
        return R.id.market_oneself_toolbar;
    }

    //自定义点击事件
    class MyListener extends OnMultiClickListener {

        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.market_message:
                    //TODO 留言板块
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
                                addMessage(market_message_edit.getText().toString()); //添加留言信息
                            }
                        }
                    });
                    break;
                case R.id.market_editor:
                    Intent intent = new Intent(getContext(), MarketPublishActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("marketModel", marketModel);
                    bundle.putString("type", "editor");
                    intent.putExtras(bundle);
                    startActivitySecurity(intent);
                    break;
                case R.id.market_delete:
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    dialog = new CustomDialog(getContext(), "温馨提示", "删除该条信息后将无法恢复，确定删除吗？");
                    dialog.setOnOkDialogListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            deleteMarketData(marketModel.getId());//删除信息
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
                    break;
                case R.id.market_remark_sale:
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    dialog = new CustomDialog(getContext(), "温馨提示", "标记后则不能更改，确定标记该物品已出售？");
                    dialog.setOnOkDialogListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            remarkMarketData(marketModel.getId());//删除信息
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
                    break;
            }
        }
    }

    //更改物品状态
    private void remarkMarketData(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        showLoading();
        Network.request(Network.createApi(IMarketApi.class).updateSaleStatus(params), new Network.IResponseListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                if (0 == Integer.parseInt(String.valueOf(data.get("flag")))) {
                    destoryActivity();
                    startActivity(new Intent(getContext(), MeMarketActivity.class));
                } else {
                    MToast.showLongToast("服务端更改出售状态失败");
                }
                hideLoading();
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showLongToast("更改物品出售状态失败");
                hideLoading();
            }
        });
    }

    //删除此条信息
    private void deleteMarketData(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        showLoading();
        Network.request(Network.createApi(IMarketApi.class).delete(params), new Network.IResponseListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                if (0 == Integer.parseInt(String.valueOf(data.get("flag")))) {
                    destoryActivity();
                    startActivity(new Intent(getContext(), MeMarketActivity.class));
                } else {
                    MToast.showLongToast("服务端删除数据失败");
                }
                hideLoading();
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showLongToast("删除数据失败");
                hideLoading();
            }
        });
    }

    //添加留言信息
    private void addMessage(String message) {
        Map<String, String> map = new HashMap<>();
        map.put("marketId", marketModel.getId());
        map.put("message", message);
        showLoading();
        Network.request(Network.createApi(IMarketApi.class).addMessage(map), new Network.IResponseListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                if (data != null) {
                    MToast.showLongToast("添加留言成功");
                    //TODO 添加留言信息成功刷新列表数据
                    MarketMessageModel marketMessageModel = JsonUtils.map2Bean(data, MarketMessageModel.class);
                    if (list.size() == 0){
                        listview.setVisibility(View.VISIBLE);
                        market_oneself_message_no_data_empty.setVisibility(View.GONE);
                        if (null == multiTypeAdapter) {
                            multiTypeAdapter = listview.initRecyclerView(list, getContext());
                        }
                    }
                    list.add(0, marketMessageModel);
                    listview.notifyDataSetChanged();
                } else {
                    MToast.showLongToast("后台添加留言信息失败");
                }
                hideLoading();
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showLongToast("添加留言信息失败");
                hideLoading();
            }
        });
    }

    //销毁指定activity
    public static void destoryActivity() {
        BaseApplication.addDestoryActivity(BaseApplication.getInstance().getCurrentActivity(), "MarketActivity");//添加到销毁队列
        BaseApplication.destoryActivity("MarketActivity"); //销毁指定的activity
        BaseApplication.addDestoryActivity(BaseApplication.getInstance().getCurrentActivity(), "MeMarketActivity");//添加到销毁队列
        BaseApplication.destoryActivity("MeMarketActivity"); //销毁指定的activity
        BaseApplication.addDestoryActivity(BaseApplication.getInstance().getCurrentActivity(), "MarketOneselfDetailActivity");//添加到销毁队列
        BaseApplication.destoryActivity("MarketOneselfDetailActivity"); //销毁指定的activity
    }
}
