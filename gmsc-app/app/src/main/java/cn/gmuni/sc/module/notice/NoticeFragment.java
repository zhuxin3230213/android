package cn.gmuni.sc.module.notice;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseFragment;
import cn.gmuni.sc.database.NoticeHistroyDao;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.InforTipsModel;
import cn.gmuni.sc.model.NoticeModel;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.INoticeApi;
import cn.gmuni.sc.utils.DateUtils;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.PixelUtil;
import cn.gmuni.sc.utils.StringUtil;
import cn.gmuni.sc.widget.CustomConstraintLayout;
import cn.gmuni.sc.widget.CustomEmptyDataView;
import cn.gmuni.sc.widget.recyclerview.ChatDetailItemDecoration;
import cn.gmuni.sc.widget.recyclerview.CustomRefreshRecycleView;
import cn.gmuni.sc.widget.recyclerview.RecycleViewDivider;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.model.Visitable;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeFragment extends BaseFragment implements CustomRefreshRecycleView.OnRefreshListener {


    @BindView(R.id.notice_list_view)
    CustomRefreshRecycleView listView;
    @BindView(R.id.notice_container)
    CustomConstraintLayout container;
    @BindView(R.id.notice_list_view_no_data_empty)
    CustomEmptyDataView notice_list_view_no_data_empty;

    private MultiTypeAdapter multiTypeAdapter;
    private static final List<Visitable> list = new ArrayList<>();


    private Integer currentPage = 1;
    private Integer pageSize = 10;
    private Integer pullDownRefreshCurrentPage = 1;
    private Integer pullDownRefreshPageSize = 10;
    private Integer loadingMoreCurrentPage = 1;
    private Integer loadingMorePageSize = 10;
    private String initDataFirstUpdateTime;
    private String initDataLastUpdateTime;
    private String lastNoticeId;
    private String initDataLastNoticeId;
    private String bottomLastNoticeId;

    private boolean isAddItemDecoration = true;
    private boolean isRCheck = false;
    private boolean isLoadingLast = true;
    private boolean isReLoadingLast = false;
    private NoticeHistroyDao noticeHistroyDao = new NoticeHistroyDao();

    public NoticeFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_notice;
    }

    @Override
    public void initView() {
        //初始化数据
        initData();
        //适配数据
        multiTypeAdapter = listView.initRefreshRecyclerView(list, getContext());
        //设置item间距
        if (isAddItemDecoration) {
            isAddItemDecoration = false;
            listView.addItemDecoration(new ChatDetailItemDecoration(PixelUtil.dp2px(10)));
        }
        //刷新事件
        listView.setOnRefreshListener(this);
    }

    @Override
    protected void managerArguments() {

    }

    //初始化数据
    private void initData() {
        list.clear();
        Map<String, String> map = new HashMap<>();
        map.put("currentPage", Integer.toString(currentPage));
        map.put("pageSize", Integer.toString(pageSize));
        getData(map);
    }

    //根据app当前显示的数量重新加载数据
    private void initReData(int pageSize) {
        if (!StringUtil.isEmpty(lastNoticeId) && !StringUtil.isEmpty(bottomLastNoticeId) && lastNoticeId.equals(bottomLastNoticeId)) {
            isReLoadingLast = true;
        }
        list.clear();
        Map<String, String> map = new HashMap<>();
        map.put("currentPage", Integer.toString(currentPage));
        map.put("pageSize", Integer.toString(pageSize));
        getData(map);
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

    @Override
    public void onLoadingMore() {
        if (!StringUtil.isEmpty(lastNoticeId) && !StringUtil.isEmpty(bottomLastNoticeId) && lastNoticeId.equals(bottomLastNoticeId)) {
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


    //下拉刷新
    private void pullDownRefreshData() {
        Map<String, String> map = new HashMap<>();
        map.put("currentPage", Integer.toString(pullDownRefreshCurrentPage));
        map.put("pageSize", Integer.toString(pullDownRefreshPageSize));
        map.put("initDataFirstUpdateTime", initDataFirstUpdateTime);
        addNoticeTop(map);
    }

    private void addNoticeTop(Map<String, String> map) {
        Network.request(Network.createApi(INoticeApi.class).addNoticeTop(map), new Network.IResponseListener<List<Map<String, String>>>() {
            @Override
            public void onSuccess(List<Map<String, String>> data) {
                if (data.size() != 0) {
                    initDataFirstUpdateTime = DateUtils.long2Data(data.get(0).get("updateTime"), DateUtils.COMMON_DATETIME);
                    handleTopData(data);
                    multiTypeAdapter.notifyDataSetChanged();
                } else {
                    int currentSize = list.size();
                    multiTypeAdapter.notifyDataSetChanged(); //解决recyclerview 多次刷新java.lang.IndexOutOfBoundsException异常处理
                    initReData(currentSize); //根据app显示列表数量重新加载数据
                }
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取数据失败");
            }
        });

    }


    //上拉加载更多
    private void onLoadingMoreData() {
        Map<String, String> map = new HashMap<>();
        map.put("currentPage", Integer.toString(loadingMoreCurrentPage));
        map.put("pageSize", Integer.toString(loadingMorePageSize));
        map.put("initDataLastUpdateTime", initDataLastUpdateTime);
        addNoticeBottom(map);
    }

    private void addNoticeBottom(Map<String, String> map) {
        Network.request(Network.createApi(INoticeApi.class).addNoticeBottom(map), new Network.IResponseListener<List<Map<String, String>>>() {
            @Override
            public void onSuccess(List<Map<String, String>> data) {
                if (data.size() != 0) {
                    initDataLastUpdateTime = DateUtils.long2Data(data.get(data.size() - 1).get("updateTime"), DateUtils.COMMON_DATETIME);
                    bottomLastNoticeId = String.valueOf(data.get(data.size() - 1).get("id"));
                    handleData(data);
                    multiTypeAdapter.notifyDataSetChanged();
                } else {
                    bottomLastNoticeId = initDataLastNoticeId;
                }
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取数据失败");
            }
        });
    }


    //网络连接获取初始数据
    private void getData(Map<String, String> map) {
        showLoading();
        Network.request(Network.createApi(INoticeApi.class).listNotices(map), new Network.IResponseListener<List<Map<String, String>>>() {
            @Override
            public void onSuccess(List<Map<String, String>> data) {
                if (data.size() != 0) {
                    notice_list_view_no_data_empty.setVisibility(View.GONE);
                    initDataFirstUpdateTime = DateUtils.long2Data(data.get(0).get("updateTime"), DateUtils.COMMON_DATETIME);
                    initDataLastUpdateTime = DateUtils.long2Data(data.get(data.size() - 2).get("updateTime"), DateUtils.COMMON_DATETIME);
                    initDataLastNoticeId = String.valueOf(data.get(data.size() - 2).get("id"));

                    lastNoticeId = String.valueOf(data.get(data.size() - 1).get("noticeId")); //获取最后一条通知id
                    handleData(data.subList(0, data.size() - 1)); //去除集合最后一条数据
                    multiTypeAdapter.notifyDataSetChanged();
                } else {
                    lastNoticeId = "";
                    hideLoading();
                    notice_list_view_no_data_empty.setVisibility(View.VISIBLE);
                    notice_list_view_no_data_empty.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            initData(); //重新初始化数据
                        }
                    });
                }
                hideLoading();
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取数据失败");
                hideLoading();
            }
        });
    }

    //处理数据
    private void handleData(List<Map<String, String>> data) {
        for (Map<String, String> temp : data) {
            NoticeModel noticeModel = new NoticeModel();
            noticeModel.setId(temp.get("id"));
            noticeModel.setTitle(temp.get("title"));
            noticeModel.setName(temp.get("deptName"));
            if (StringUtil.isEmpty(temp.get("updateTime"))) {
                noticeModel.setUpdateTime(null);
            } else {
                noticeModel.setUpdateTime(DateUtils.long2Data(temp.get("updateTime"), "yyyy-MM-dd HH:mm:ss").substring(0, 10));
            }
            noticeModel.setContent(temp.get("description"));
            list.add(noticeModel);
            //noticeHistroyDao.deleteAll(); //清空通知阅读状态
        }
    }

    //处理数据
    private void handleTopData(List<Map<String, String>> data) {
        Collections.reverse(data);
        for (Map<String, String> temp : data) {
            NoticeModel noticeModel = new NoticeModel();
            noticeModel.setId(temp.get("id"));
            noticeModel.setTitle(temp.get("title"));
            noticeModel.setName(temp.get("deptName"));
            if (StringUtil.isEmpty(temp.get("updateTime"))) {
                noticeModel.setUpdateTime(null);
            } else {
                noticeModel.setUpdateTime(DateUtils.long2Data(temp.get("updateTime"), "yyyy-MM-dd HH:mm:ss").substring(0, 10));
            }
            noticeModel.setContent(temp.get("description"));
            list.add(0, noticeModel);
        }
    }

    public static NoticeFragment newInstance() {
        NoticeFragment fragment = new NoticeFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public CustomConstraintLayout getContainer() {
        return container;
    }
}
