package cn.gmuni.sc.module.more.market;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.MarketMessageModel;
import cn.gmuni.sc.model.MarketModel;
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
import cn.gmuni.sc.widget.CustomEmptyDataView;
import cn.gmuni.sc.widget.CustomImageView;
import cn.gmuni.sc.widget.CustomReportDetailDialog;
import cn.gmuni.sc.widget.CustomReportDialog;
import cn.gmuni.sc.widget.recyclerview.CustomRefreshRecycleView;
import cn.gmuni.sc.widget.recyclerview.RecycleViewDivider;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.model.Visitable;

public class MarketOthersDetailActivity extends BaseCommonActivity {

    @BindView(R.id.market_other_message_list_view)
    CustomRefreshRecycleView listview; //留言列表
    @BindView(R.id.market_other_detail_user_img)
    CircleImageView market_other_detail_user_img; //头像
    @BindView(R.id.market_other_detail_nick_name)
    TextView market_other_detail_nick_name; //昵称
    @BindView(R.id.market_other_detail_update_time)
    TextView market_other_detail_update_time; //更新时间
    @BindView(R.id.market_other_detail_campus)
    TextView market_other_detail_campus; //学校
    @BindView(R.id.market_other_detail_images)
    CustomImageView market_other_detail_images; //拍照图片（可能多个）
    @BindView(R.id.market_other_detail_price)
    TextView market_other_detail_price; //价格
    @BindView(R.id.market_other_detail_title)
    TextView market_other_detail_title; //标题
    @BindView(R.id.market_other_detail_introduce)
    TextView market_other_detail_introduce; //简介
    @BindView(R.id.marke_message_count)
    TextView marke_message_count; //留言统计
    @BindView(R.id.marke_other_detail_message_count)
    TextView marke_other_detail_message_count; //留言统计

    @BindView(R.id.marke_other_detail_leave_message)
    CustomImageView marke_other_detail_leave_message; //留言

    @BindView(R.id.market_operation_layout)
    LinearLayout market_operation_layout;
    @BindView(R.id.market_message_layout)
    LinearLayout market_message_layout;
    @BindView(R.id.market_message_edit)
    ContainsEmojiEditText market_message_edit;

    @BindView(R.id.market_message_send)
    TextView market_message_send;


    @BindView(R.id.market_other_message_no_data_empty)
    CustomEmptyDataView market_other_message_no_data_empty;
    private MultiTypeAdapter multiTypeAdapter;
    private static final List<Visitable> list = new ArrayList<>();
    protected final Map<String, String> params = new HashMap<>();

    private boolean isAddItemDecoration = true;
    //定义一个标志位，用于标记用户已经将列表拉到底了
    private boolean isLast = false;
    //定义一个标志位，用于标记页面正在加载数据
    private boolean isLoading = false;
    private MarketModel marketModel;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_market_others_detail);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        marketModel = (MarketModel) getIntent().getSerializableExtra("marketModel");
        //头像
        if (!StringUtil.isEmpty(marketModel.getAvatar())) {
            Bitmap snapshotBm = BitMapUtil.base64ToBitmap(marketModel.getAvatar());
            market_other_detail_user_img.setImageBitmap(snapshotBm);
        }
        market_other_detail_nick_name.setText(marketModel.getUserName());
        market_other_detail_update_time.setText(marketModel.getUpdateTime());
        market_other_detail_campus.setText(marketModel.getSchoolName());
        market_other_detail_price.setText(marketModel.getPrice());
        market_other_detail_title.setText(marketModel.getTitle());
        market_other_detail_introduce.setText(marketModel.getIntroduce());
        getImgBData(marketModel.getId()); //获取大图
        //添加分割线
        if (isAddItemDecoration) {
            isAddItemDecoration = false;
            listview.addItemDecoration(new RecycleViewDivider(0, PixelUtil.dp2px(1)));
        }
        //刷新事件
        //listview.setOnRefreshListener(this);
        //事件
        bindEvent(marketModel.getId());
    }

    @Override
    public int getToolbar() {
        return R.id.market_other_toolbar;
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
                    market_other_detail_images.setImageBitmap(BitMapUtil.base64ToBitmap(data.getImgB()));
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
            market_other_message_no_data_empty.setVisibility(View.GONE);
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
                    multiTypeAdapter.setTag("1");
                }
            } else {
                listview.setVisibility(View.GONE);
                market_other_message_no_data_empty.setVisibility(View.VISIBLE);
            }

        } else {
            listview.setVisibility(View.GONE);
            market_other_message_no_data_empty.setVisibility(View.VISIBLE);
        }

    }

    //事件
    private void bindEvent(String id) {
        toolbar.setOnActionListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                final CustomReportDialog dialog = new CustomReportDialog(getContext());
                dialog.show(); //显示
                //调用这个方法时，按对话框以外的地方无法响应。按返回键还可以响应。
                dialog.setCanceledOnTouchOutside(false);
                //调用这个方法时，按对话框以外的地方和按返回键都无法响应。
                dialog.setCancelable(false);
                //设置弹窗大小、位置
                //获取屏幕宽度
                DisplayMetrics dm = getResources().getDisplayMetrics();
                int screenWidth = dm.widthPixels;
                int screenHeight = dm.heightPixels;
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                // window.getDecorView().setPadding(PixelUtil.dp2px(75), 0, PixelUtil.dp2px(75), PixelUtil.dp2px(40));

                final WindowManager.LayoutParams params = window.getAttributes();
                params.gravity = Gravity.BOTTOM;
                  /*  params.width = screenWidth - PixelUtil.dp2px(150);
                    params.height = screenHeight- PixelUtil.dp2px(150);*/
                dialog.getWindow().setAttributes(params);
                dialog.setClicklistener(new CustomReportDialog.ClickListenerInterface() {
                    @Override
                    public void doCancel() {
                        //取消
                        dialog.dismiss();
                    }

                    @Override
                    public void doSubmit() {
                        //取消
                        dialog.dismiss();
                        //TODO 弹窗待开发
                        final CustomReportDetailDialog detailDialog = new CustomReportDetailDialog(getContext());
                        detailDialog.show(); //显示
                        //调用这个方法时，按对话框以外的地方无法响应。按返回键还可以响应。
                        detailDialog.setCanceledOnTouchOutside(false);
                        //调用这个方法时，按对话框以外的地方和按返回键都无法响应。
                        detailDialog.setCancelable(false);
                        //设置弹窗大小、位置
                        //获取屏幕宽度
                        DisplayMetrics dm = getResources().getDisplayMetrics();
                        int screenWidth = dm.widthPixels;
                        int screenHeight = dm.heightPixels;
                        Window window = detailDialog.getWindow();
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        //window.getDecorView().setPadding(PixelUtil.dp2px(75), 0, PixelUtil.dp2px(75), 0);

                        final WindowManager.LayoutParams params = window.getAttributes();
                        params.gravity = Gravity.CENTER;
                        params.width = screenWidth - PixelUtil.dp2px(150);
                        params.height = screenHeight - PixelUtil.dp2px(250);
                        detailDialog.getWindow().setAttributes(params);
                        detailDialog.setClicklistener(new CustomReportDetailDialog.ClickListenerInterface() {
                            @Override
                            public void doCancel() {
                                //取消
                                detailDialog.dismiss();
                            }

                            @Override
                            public void doSubmit(String title) {
                                //提交举报
                                confirmReport(id, title);
                                detailDialog.dismiss();
                            }
                        });
                    }
                });
            }
        });
        //留言
        marke_other_detail_leave_message.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                market_operation_layout.setVisibility(View.GONE);
                market_message_layout.setVisibility(View.VISIBLE);
                market_message_edit.setFocusable(true);
                market_message_edit.setFocusableInTouchMode(true);
                market_message_edit.requestFocus();
                KeyBoardUtil.showInputMethod(market_message_edit.getContext()); //调用键盘
                market_message_send.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        market_operation_layout.setVisibility(View.VISIBLE);
                        market_message_layout.setVisibility(View.GONE);
                        KeyBoardUtil.hideKeyboard(market_message_edit); //隐藏键盘
                        addMessage(market_message_edit.getText().toString()); //添加留言信息
                    }
                });
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
                        market_other_message_no_data_empty.setVisibility(View.GONE);
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

    //提交举报信息
    private void confirmReport(String id, String title) {
        Map<String, String> map = new HashMap<>();
        map.put("marketId", id);
        map.put("reason", title);
        addReport(map);//添加举报信息
    }

    //添加举报信息
    private void addReport(Map<String, String> map) {
        showLoading();
        Network.request(Network.createApi(IMarketApi.class).addReport(map), new Network.IResponseListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                if (Boolean.parseBoolean(String.valueOf(data.get("flag")))) {
                    MToast.showLongToast("举报成功");
                } else {
                    MToast.showLongToast("后台添加举报信息失败");
                }
                hideLoading();
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showLongToast("添加举报信息失败");
                hideLoading();
            }
        });
    }
}
