package cn.gmuni.sc.module.me.message.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.base.BaseFragment;
import cn.gmuni.sc.database.UserInfoDao;
import cn.gmuni.sc.model.InforTipsModel;
import cn.gmuni.sc.model.MessageModel;
import cn.gmuni.sc.module.me.MeFragment;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.IMessageApi;
import cn.gmuni.sc.utils.DateUtils;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.StringUtil;
import cn.gmuni.sc.widget.recyclerview.CustomRefreshRecycleView;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.model.Visitable;

public class MessageFragment extends BaseFragment implements CustomRefreshRecycleView.OnRefreshListener {

    CustomRefreshRecycleView listView;
    private static final List<Visitable> list = new ArrayList<>();
    private MultiTypeAdapter multiTypeAdapter;
    protected final Map<String, String> params = new HashMap<>();

    private UserInfoDao userInfoDao = new UserInfoDao();
    //初始化
    private Integer currentPage = 1;
    private Integer pageSize = 10;
    //下拉刷新
    private Integer pullDownRefreshCurrentPage = 1;
    private Integer pullDownRefreshPageSize = 10;
    //上拉加载
    private Integer loadingMoreCurrentPage = 1;
    private Integer loadingMorePageSize = 10;
    //渲染列表第一条数据与最后一条时间
    private String initDataFirstTime;
    private String initDataLastTime;

    //消息列表最后一条数据
    private String initDataLastMessageId;
    private String lastMessageId;
    private String bottomLastMessageId;

    //上拉加载限制标记
    private boolean isLoadingLast = true;
    private boolean isReLoadingLast = false;

    private boolean iState;

    /**
     * 初始化布局
     *
     * @return 布局文件的id。
     */
    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    protected void managerArguments() {

    }

    public void showEmpty() {

    }

    public void hideEmpty() {

    }

    /**
     * 获取渲染列表的id
     *
     * @return
     */
    public int getListId() {
        return 0;
    }

    @Override
    public void initView() {
        listView = (CustomRefreshRecycleView) findViewById(getListId());
        initData(); //初始化数据
        //适配数据
        multiTypeAdapter = listView.initRefreshRecyclerView(list, this.getContext());
        //刷新事件
        listView.setOnRefreshListener(this);
    }

    //返回之前
    public void beforeGoBack() {
        Intent intent = new Intent(getContext(), MeFragment.class);
        Bundle bundle = new Bundle();
        if (iState) {
            bundle.putString("iState", "true");
        } else {
            bundle.putString("iState", "false");
        }
        intent.putExtras(bundle);
        BaseApplication.getInstance().getCurrentActivity().setResult(MeFragment.READ_MESSAGE_STATE, intent);
    }

    //初始化数据
    private void initData() {
        list.clear();
        params.put("currentPage", Integer.toString(currentPage));
        params.put("pageSize", Integer.toString(pageSize));
        getData(params);
    }

    //根据app当前显示的数量重新加载数据
    private void initReData(int pageSize) {
        if (!StringUtil.isEmpty(lastMessageId) && !StringUtil.isEmpty(bottomLastMessageId) && lastMessageId.equals(bottomLastMessageId)) {
            isReLoadingLast = true;
        }
        list.clear();
        params.put("currentPage", Integer.toString(currentPage));
        params.put("pageSize", Integer.toString(pageSize));
        getData(params);
    }

    //联网获取数据
    private void getData(Map<String, String> map) {
        showLoading();
        Network.request(Network.createApi(IMessageApi.class).listMessage(map), new Network.IResponseListener<List<Map<String, Object>>>() {
            @Override
            public void onSuccess(List<Map<String, Object>> data) {
                if (data.size() != 0) {
                    hideEmpty();
                    initDataFirstTime = DateUtils.long2Data(String.valueOf(data.get(0).get("create_time")), DateUtils.COMMON_DATETIME);
                    initDataLastTime = DateUtils.long2Data(String.valueOf(data.get(data.size() - 2).get("create_time")), DateUtils.COMMON_DATETIME);
                    initDataLastMessageId = String.valueOf(data.get(data.size() - 2).get("id"));

                    lastMessageId = String.valueOf(data.get(data.size() - 1).get("id")); //获取最后一条数据id
                    handleData(data.subList(0, data.size() - 1)); //获取数据,并去除集合最后一条数据
                    multiTypeAdapter.notifyDataSetChanged();
                } else {
                    showEmpty();
                    //空数据时返回状态
                    iState = false;
                    beforeGoBack();
                }
                hideLoading();
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("初始化化数据失败");
                hideLoading();
            }
        });
    }

    //处理初始化数据
    private void handleData(List<Map<String, Object>> data) {
        List<String> strings = new ArrayList<>();
        for (Map<String, Object> temp : data) {
            MessageModel messageModel = new MessageModel(); //消息
            messageModel.setId(String.valueOf(temp.get("id")));
            messageModel.setPublisher(String.valueOf(temp.get("publisher")));
            messageModel.setDeptName(String.valueOf(temp.get("dept_name")));
            messageModel.setTime(DateUtils.long2Data(String.valueOf(temp.get("create_time")), "yyyy-MM-dd HH:mm:ss"));
            messageModel.setTypeName(String.valueOf(temp.get("type")));
            messageModel.setContent(String.valueOf(temp.get("content")));
            messageModel.setState(String.valueOf(temp.get("state")));
            messageModel.setUserInfo(userInfoDao.get().getName());
            messageModel.setTitle(String.valueOf(temp.get("title")));
            messageModel.setNetId(String.valueOf(temp.get("net_id")));
            messageModel.setNetType(String.valueOf(temp.get("net_type")));
            messageModel.setStudentCode(String.valueOf(temp.get("student_code")));
            messageModel.setTotalAmount(String.valueOf(temp.get("total_amount")));
            messageModel.setTradeNo(String.valueOf(temp.get("trade_no")));
            strings.add(String.valueOf(temp.get("state")));
            list.add(messageModel);
        }
        iState = strings.contains("0"); //有未读消息  0：未读 1：已读
        beforeGoBack(); //返回操作
    }

    @Override
    public void onPullDownRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullDownRefreshData();
                multiTypeAdapter.notifyDataSetChanged();
                listView.hideHeaderView();
            }
        }, 2000);
    }

    //下拉刷新
    private void pullDownRefreshData() {
        params.put("currentPage", Integer.toString(pullDownRefreshCurrentPage));
        params.put("pageSize", Integer.toString(pullDownRefreshPageSize));
        params.put("initDataFirstTime", initDataFirstTime);
        addMessageTop(params);
    }

    //获取下拉刷新数据
    private void addMessageTop(Map<String, String> map) {
        Network.request(Network.createApi(IMessageApi.class).addMessageTop(map), new Network.IResponseListener<List<Map<String, Object>>>() {
            @Override
            public void onSuccess(List<Map<String, Object>> data) {
                if (data.size() != 0) {
                    initDataFirstTime = DateUtils.long2Data(String.valueOf(data.get(0).get("create_time")), DateUtils.COMMON_DATETIME);
                    handleTopData(data); //处理数据
                    multiTypeAdapter.notifyDataSetChanged();
                } else {
                    int currentSize = list.size();
                    multiTypeAdapter.notifyDataSetChanged(); //解决recyclerview 多次刷新java.lang.IndexOutOfBoundsException异常处理
                    initReData(currentSize); //根据app显示列表数量重新加载数据
                }
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("下拉刷新数据失败");
            }
        });
    }

    //处理下拉刷新数据
    private void handleTopData(List<Map<String, Object>> data) {
        Collections.reverse(data);
        List<String> strings = new ArrayList<>();
        for (Map<String, Object> temp : data) {
            MessageModel messageModel = new MessageModel(); //消息
            messageModel.setId(String.valueOf(temp.get("id")));
            messageModel.setPublisher(String.valueOf(temp.get("publisher")));
            messageModel.setDeptName(String.valueOf(temp.get("dept_name")));
            messageModel.setTime(DateUtils.long2Data(String.valueOf(temp.get("create_time")), "yyyy-MM-dd HH:mm:ss"));
            messageModel.setTypeName(String.valueOf(temp.get("type")));
            messageModel.setContent(String.valueOf(temp.get("content")));
            messageModel.setState(String.valueOf(temp.get("state")));
            messageModel.setUserInfo(userInfoDao.get().getName());
            messageModel.setTitle(String.valueOf(temp.get("title")));
            messageModel.setNetId(String.valueOf(temp.get("net_id")));
            messageModel.setNetType(String.valueOf(temp.get("net_type")));
            messageModel.setStudentCode(String.valueOf(temp.get("student_code")));
            messageModel.setTotalAmount(String.valueOf(temp.get("total_amount")));
            messageModel.setTradeNo(String.valueOf(temp.get("trade_no")));
            strings.add(String.valueOf(temp.get("state")));
            list.add(0, messageModel);
        }
        iState = strings.contains("0"); //有未读消息
        beforeGoBack();
    }


    @Override
    public void onLoadingMore() {
        if (!StringUtil.isEmpty(lastMessageId) && !StringUtil.isEmpty(bottomLastMessageId) && lastMessageId.equals(bottomLastMessageId)) {
            listView.hideFooterView(); //隐藏底部布局
            if (isLoadingLast) {
                isLoadingLast = false;
                if (isReLoadingLast) {
                    //如果是下拉刷新加载最后一条时，不加载提示
                } else {
                    InforTipsModel inforTipsModel = new InforTipsModel();
                    inforTipsModel.setInformationTips("暂无更多数据");
                    list.add(inforTipsModel);
                    multiTypeAdapter.notifyDataSetChanged();
                }

            } else {
                //这里分为两种情况：一直上拉加载 / 下拉刷新时
                if (isReLoadingLast) {//当满足条件时下拉刷新
                    isReLoadingLast = false;
                    isLoadingLast = false;
                    InforTipsModel inforTipsModel = new InforTipsModel();
                    inforTipsModel.setInformationTips("暂无更多数据");
                    list.add(inforTipsModel);
                    multiTypeAdapter.notifyDataSetChanged();
                } else {
                }

            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onLoadingMoreData();
                    multiTypeAdapter.notifyDataSetChanged();
                    listView.hideFooterView();
                }
            }, 5000);
        }

    }

    //上拉加载获取数据
    private void onLoadingMoreData() {
        params.put("currentPage", Integer.toString(loadingMoreCurrentPage));
        params.put("pageSize", Integer.toString(loadingMorePageSize));
        params.put("initDataLastTime", initDataLastTime);
        addMessageBottom(params);
    }

    //获取上拉加载数据
    private void addMessageBottom(Map<String, String> map) {
        Network.request(Network.createApi(IMessageApi.class).addMessageBottom(map), new Network.IResponseListener<List<Map<String, Object>>>() {
            @Override
            public void onSuccess(List<Map<String, Object>> data) {
                if (data.size() != 0) {
                    initDataLastTime = DateUtils.long2Data(String.valueOf(data.get(data.size() - 1).get("create_time")), DateUtils.COMMON_DATETIME);
                    bottomLastMessageId = String.valueOf(data.get(data.size() - 1).get("id"));
                    handleData(data);
                    multiTypeAdapter.notifyDataSetChanged();
                } else {
                    bottomLastMessageId = initDataLastMessageId;
                }
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("上拉加载获取数据失败");
            }
        });
    }

    //刷新fragment数据
    public void refeshFragment() {
        initData(); //重新加载数据
    }

}
